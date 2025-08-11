package com.example.kangpismanapp.data.repository

import android.util.Log
import com.example.kangpismanapp.data.model.Reward
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardRepository @Inject constructor() {
    private val db = Firebase.firestore

    suspend fun getRewards(): List<Reward> {
        return try {
            Log.d("REWARD_DEBUG", "Repository: Mengambil data dari koleksi 'rewards'...")
            val querySnapshot = db.collection("rewards").get().await()
            Log.d("REWARD_DEBUG", "Repository: Sukses! Ditemukan ${querySnapshot.size()} dokumen reward.")
            querySnapshot.toObjects(Reward::class.java)
        } catch (e: Exception) {
            Log.e("REWARD_DEBUG", "Repository: Gagal mengambil data reward!", e)
            emptyList()
        }
    }
}