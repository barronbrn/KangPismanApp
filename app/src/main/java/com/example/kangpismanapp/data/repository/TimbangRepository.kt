package com.example.kangpismanapp.data.repository

import android.util.Log
import com.example.kangpismanapp.data.model.DraftTransaksi
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

    suspend fun updateDraftStatus(draftId: String, newStatus: String): Boolean {
        return try {
            db.collection("draft_transaksi").document(draftId)
                .update("status", newStatus).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getDaftarMaterial(): List<Sampah> {
        return try {
            Log.d("BUAT_SETORAN_DEBUG", "Repository: Mengambil daftar material...")
            val querySnapshot = db.collection("harga_material").get().await()
            Log.d("BUAT_SETORAN_DEBUG", "Repository: Sukses! Ditemukan ${querySnapshot.size()} dokumen material.")
            querySnapshot.toObjects(Sampah::class.java)
        } catch (e: Exception) {
            Log.e("BUAT_SETORAN_DEBUG", "Repository: Gagal mengambil material!", e)
            emptyList()
        }
    }

    suspend fun simpanTransaksi(transaksi: Transaksi): Boolean {
        return try {
            db.collection("transaksi").add(transaksi).await()
            true // Sukses
        } catch (e: Exception) {
            false // Gagal
        }
    }

    suspend fun getDraftById(draftId: String): DraftTransaksi? {
        return try {
            db.collection("draft_transaksi").document(draftId).get().await()
                .toObject(DraftTransaksi::class.java)
        } catch (e: Exception) {
            null
        }
    }
}