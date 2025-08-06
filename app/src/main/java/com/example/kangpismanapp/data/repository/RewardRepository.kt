package com.example.kangpismanapp.data.repository

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
            db.collection("rewards").get().await().toObjects(Reward::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}