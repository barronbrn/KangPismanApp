package com.example.kangpismanapp.data.repository

import android.util.Log
import com.example.kangpismanapp.data.model.Sampah
import com.example.kangpismanapp.data.model.Transaksi
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimbangRepository @Inject constructor() {
    private val db = Firebase.firestore

    suspend fun getDaftarMaterial(): List<Sampah> {
        return try {
            Log.d("APP_DEBUG_TIMBANG", "Repository: Mengambil daftar material...") // LOG 1
            val querySnapshot = db.collection("harga_material").get().await()
            Log.d("APP_DEBUG_TIMBANG", "Repository: Sukses! Ditemukan ${querySnapshot.size()} dokumen material.") // LOG 2
            querySnapshot.toObjects(Sampah::class.java)
        } catch (e: Exception) {
            Log.e("APP_DEBUG_TIMBANG", "Repository: Gagal mengambil material!", e) // LOG 3
            emptyList()
        }
    }

    // Menyimpan objek transaksi ke firestore
    suspend fun simpanTransaksi(transaksi: Transaksi): Boolean {
        return try {
            db.collection("transaksi").add(transaksi).await()
            true // Sukses
        } catch (e: Exception) {
            false // Gagal
        }
    }
}