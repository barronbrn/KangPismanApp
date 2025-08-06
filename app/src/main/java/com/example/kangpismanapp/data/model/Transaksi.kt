package com.example.kangpismanapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

// Model untuk satu item sampah dalam satu transaksi
data class ItemTransaksi(
    val namaMaterial: String = "",
    val hargaPerKg: Int = 0,
    val beratKg: Double = 0.0,
    val subtotal: Int = 0
)

// Model untuk satu transaksi keseluruhan
data class Transaksi(
    val uid: String = "", // ID pengguna
    @ServerTimestamp
    val tanggal: Date? = null, // Tanggal transaksi
    val totalPoin: Int = 0, // Total poin/saldo dari transaksi
    val items: List<ItemTransaksi> = emptyList() // Daftar item yang ditimbang
)
