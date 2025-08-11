package com.example.kangpismanapp.data.repository

import com.example.kangpismanapp.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.code


@Singleton
class ProfileRepository @Inject constructor() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun getUserProfileData(): Flow<UserProfile?> = callbackFlow {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val userDocRef = db.collection("users").document(currentUser.uid)

        val listenerRegistration = db.collection("transaksi")
            .whereEqualTo("uid", currentUser.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // ... (logika penanganan error tidak berubah) ...
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    // 1. Hitung total poin (seperti sebelumnya)
                    val totalPoin = snapshot.sumOf { doc -> doc.getLong("totalPoin")?.toInt() ?: 0 }

                    // 2. Hitung total berat DAN total saldo (Rupiah) baru
                    var totalBerat = 0.0
                    var totalSaldoRupiah = 0
                    snapshot.forEach { doc ->
                        val items = doc.get("items") as? List<Map<String, Any>>
                        items?.forEach { itemMap ->
                            val berat = itemMap["beratKg"] as? Double ?: 0.0
                            val harga = itemMap["hargaPerKg"] as? Long ?: 0L
                            totalBerat += berat
                            totalSaldoRupiah += (berat * harga).toInt()
                        }
                    }

                    // 3. Ambil data profil lain dan gabungkan semuanya
                    userDocRef.get().addOnSuccessListener { userDoc ->
                        val username = userDoc.getString("username") ?: ""
                        // ... ambil data lain (noTelepon, alamat, imageUrl) ...

                        val fullProfile = UserProfile(
                            email = currentUser.email ?: "",
                            username = username,
                            // ... data lain ...
                            totalPoin = totalPoin,
                            totalSaldo = totalSaldoRupiah, // <-- Gunakan total saldo baru
                            totalBeratKg = totalBerat
                        )
                        trySend(fullProfile)
                    }
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    suspend fun updateUserProfile(username: String, noTelepon: String, alamat: String): Boolean {
        val uid = auth.currentUser?.uid ?: return false // Hentikan jika pengguna tidak login
        return try {
            // Buat map hanya untuk data yang akan di-update
            val userUpdates = mapOf(
                "username" to username,
                "noTelepon" to noTelepon,
                "alamat" to alamat
            )
            // Panggil fungsi update dari Firestore
            db.collection("users").document(uid).update(userUpdates).await()
            true // Kembalikan true jika sukses
        } catch (e: Exception) {
            false // Kembalikan false jika gagal
        }
    }
}