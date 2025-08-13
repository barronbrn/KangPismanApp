package com.example.kangpismanapp.data.repository

import com.example.kangpismanapp.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.code


@Singleton
class ProfileRepository @Inject constructor() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun getUserProfileData(): Flow<UserProfile?> {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            return callbackFlow { trySend(null); close() }
        }

        // 1. Buat Flow untuk mengambil data dari koleksi 'users' secara real-time
        val userProfileFlow: Flow<UserProfile> = callbackFlow {
            val userDocRef = db.collection("users").document(currentUser.uid)
            val listener = userDocRef.addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    val namaLengkap = snapshot.getString("namaLengkap") ?: ""
                    val username = snapshot.getString("username") ?: ""
                    val noTelepon = snapshot.getString("noTelepon") ?: ""
                    val alamat = snapshot.getString("alamat") ?: ""
                    val imageUrl = snapshot.getString("profileImageUrl") ?: ""
                    trySend(UserProfile(currentUser.email ?: "", username, noTelepon, alamat, imageUrl))
                } else {
                    // Kirim profil dasar jika dokumen belum ada
                    trySend(UserProfile(email = currentUser.email ?: ""))
                }
            }
            awaitClose { listener.remove() }
        }

        // 2. Buat Flow untuk mengambil data dari koleksi 'transaksi' secara real-time
        val transactionDataFlow: Flow<Triple<Int, Int, Double>> = callbackFlow {
            val listener = db.collection("transaksi")
                .whereEqualTo("uid", currentUser.uid)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        val totalSaldo = snapshot.sumOf { doc -> doc.getLong("totalRupiah")?.toInt() ?: 0 }
                        val totalPoin = snapshot.sumOf { doc -> doc.getLong("totalPoin")?.toInt() ?: 0 }
                        var totalBerat = 0.0
                        snapshot.forEach { doc ->
                            val items = doc.get("items") as? List<Map<String, Any>>
                            items?.forEach { itemMap ->
                                totalBerat += itemMap["beratKg"] as? Double ?: 0.0
                            }
                        }
                        trySend(Triple(totalSaldo, totalPoin, totalBerat))
                    } else {
                        // Kirim nilai nol jika tidak ada transaksi
                        trySend(Triple(0, 0, 0.0))
                    }
                }
            awaitClose { listener.remove() }
        }

        // 3. Gabungkan kedua hasil Flow menjadi satu
        return userProfileFlow.combine(transactionDataFlow) { profile, transactionData ->
            profile.copy(
                totalSaldo = transactionData.first,
                totalPoin = transactionData.second,
                totalBeratKg = transactionData.third
            )
        }
    }

    suspend fun updateUserProfile(
        namaLengkap: String,
        username: String,
        noTelepon: String,
        alamat: String
    ): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        return try {
            val userUpdates = mapOf(
                "namaLengkap" to namaLengkap,
                "username" to username,
                "noTelepon" to noTelepon,
                "alamat" to alamat
            )
            db.collection("users").document(uid).update(userUpdates).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserRole(): String? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            db.collection("users").document(uid).get().await().getString("role")
        } catch (e: Exception) {
            null
        }
    }
}