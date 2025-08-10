package com.example.kangpismanapp.data.repository

import android.util.Log
import com.example.kangpismanapp.data.model.Artikel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EdukasiRepository @Inject constructor() {
    private val db = Firebase.firestore
    suspend fun getArticles(): List<Artikel> {
        return try {
            Log.d("EDUKASI_DEBUG", "Repository: Mengambil artikel dari koleksi 'artikel'...")
            val querySnapshot = db.collection("artikel").get().await()
            Log.d("EDUKASI_DEBUG", "Repository: Sukses! Ditemukan ${querySnapshot.size()} dokumen artikel.")
            querySnapshot.toObjects(Artikel::class.java)
        } catch (e: Exception) {
            Log.e("EDUKASI_DEBUG", "Repository: Gagal mengambil artikel!", e)
            emptyList()
        }
    }
}