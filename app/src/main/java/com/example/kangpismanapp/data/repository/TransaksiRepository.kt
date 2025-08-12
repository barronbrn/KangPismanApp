package com.example.kangpismanapp.data.repository

import com.example.kangpismanapp.data.model.Transaksi
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransaksiRepository @Inject constructor() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    fun getTransaksiHistoryForWarga(): Flow<List<Transaksi>> = callbackFlow {
        val uid = auth.currentUser?.uid ?: return@callbackFlow
        val listenerRegistration = db.collection("transaksi")
            .whereEqualTo("uid", uid) // <-- Mencari berdasarkan UID Warga
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    if (error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        trySend(emptyList())
                    } else {
                        close(error)
                    }
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(Transaksi::class.java))
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    fun getTransaksiHistoryForPetugas(): Flow<List<Transaksi>> = callbackFlow {
        val uid = auth.currentUser?.uid ?: return@callbackFlow
        val listenerRegistration = db.collection("transaksi")
            .whereEqualTo("petugasUid", uid) // <-- Mencari berdasarkan UID Petugas
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    if (error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        trySend(emptyList())
                    } else {
                        close(error)
                    }
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(Transaksi::class.java))
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    fun getLatestTransaksi(limit: Long = 3): Flow<List<Transaksi>> = callbackFlow {
        val uid = auth.currentUser?.uid ?: return@callbackFlow
        val listenerRegistration = db.collection("transaksi")
            .whereEqualTo("uid", uid)
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .limit(limit)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    if (error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        trySend(emptyList()) // Jika ditolak, kirim list kosong
                    } else {
                        close(error)
                    }
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(Transaksi::class.java))
                }
            }
        awaitClose { listenerRegistration.remove() }
    }
}