package com.example.kangpismanapp.data.repository

import com.example.kangpismanapp.data.model.Transaksi
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransaksiRepository @Inject constructor() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    // LENGKAPI FUNGSI INI
    suspend fun getTransaksiHistory(): List<Transaksi> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            db.collection("transaksi")
                .whereEqualTo("uid", uid)
                .orderBy("tanggal", Query.Direction.DESCENDING)
                .get().await().toObjects(Transaksi::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Fungsi yang sudah ada
    suspend fun getLatestTransaksi(limit: Long = 3): List<Transaksi> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            db.collection("transaksi")
                .whereEqualTo("uid", uid)
                .orderBy("tanggal", Query.Direction.DESCENDING)
                .limit(limit)
                .get().await().toObjects(Transaksi::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}