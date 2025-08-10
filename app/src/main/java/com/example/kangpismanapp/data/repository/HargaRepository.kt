package com.example.kangpismanapp.data.repository

import android.util.Log
import com.example.kangpismanapp.data.model.Sampah
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HargaRepository @Inject constructor() {

    private val db = Firebase.firestore

    suspend fun getAllDaftarHarga(): List<Sampah> {
        return try {
            db.collection("harga_material").get().await().toObjects(Sampah::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getDaftarHarga(): List<Sampah> {
        return try {
            val querySnapshot = db.collection("harga_material")
                .limit(4)
                .get().await()
            querySnapshot.toObjects(Sampah::class.java)
        } catch (e: Exception) {
            Log.e("HargaRepository", "Error getting documents: ", e)
            emptyList()
        }
    }
}