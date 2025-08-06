package com.example.kangpismanapp.data.repository

import com.example.kangpismanapp.data.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(){
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    suspend fun getUserProfileData(): UserProfile? {
        val currentUser = auth.currentUser ?: return null
        val email = currentUser.email ?: "Email tidak ditemukan"

        return try {
            val querySnapshot = db.collection("transaksi")
                .whereEqualTo("uid", currentUser.uid)
                .get().await()

            val totalPoin = querySnapshot.sumOf { doc ->
                doc.getLong("totalPoin")?.toInt() ?: 0
            }

            UserProfile(email, totalPoin)
        } catch (e: Exception) {
            UserProfile(email, 0) // Kembalikan 0 jika ada error
        }
    }
}