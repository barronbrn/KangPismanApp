package com.example.kangpismanapp.data.repository

import com.example.kangpismanapp.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor() {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun getUserProfileData(): Flow<UserProfile?> = callbackFlow {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            trySend(null)
            return@callbackFlow
        }

        val listenerRegistration = db.collection("transaksi")
            .whereEqualTo("uid", currentUser.uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    if (error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        // Jika ditolak, kirim profil default
                        trySend(UserProfile(currentUser.email ?: "", 0, 0.0))
                    } else {
                        close(error)
                    }
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val totalPoin = snapshot.sumOf { doc -> doc.getLong("totalPoin")?.toInt() ?: 0 }
                    var totalBerat = 0.0
                    snapshot.forEach { doc ->
                        val items = doc.get("items") as? List<Map<String, Any>>
                        items?.forEach { itemMap ->
                            totalBerat += itemMap["beratKg"] as? Double ?: 0.0
                        }
                    }
                    trySend(UserProfile(currentUser.email ?: "", totalPoin, totalBerat))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}