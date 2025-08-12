package com.example.kangpismanapp.data.repository

import android.util.Log
import com.example.kangpismanapp.data.model.DraftTransaksi
import com.example.kangpismanapp.data.model.Sampah
import com.example.kangpismanapp.data.model.Transaksi
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    fun getAllPendingDrafts(): Flow<List<DraftTransaksi>> = callbackFlow {
        val listenerRegistration = db.collection("draft_transaksi")
            .whereEqualTo("status", "pending")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val drafts = snapshot.toObjects(DraftTransaksi::class.java)
                    trySend(drafts)
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    suspend fun getDaftarMaterial(): List<Sampah> {
        return try {
            val querySnapshot = db.collection("harga_material").get().await()
            querySnapshot.toObjects(Sampah::class.java)
        } catch (e: Exception) {
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