package com.example.kangpismanapp.data.repository

import com.example.kangpismanapp.data.model.BankSampah
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LokasiRepository @Inject constructor() {
    private val db = Firebase.firestore

    suspend fun getBankSampahList(): List<BankSampah> {
        return try {
            db.collection("bank_sampah").get().await().toObjects(BankSampah::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}