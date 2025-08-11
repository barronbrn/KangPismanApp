package com.example.kangpismanapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

// Model untuk item di dalam draft
data class ItemDraft(
    val namaMaterial: String = "",
    val estimasiBeratKg: Double = 0.0
)

// Model untuk dokumen draft transaksi
data class DraftTransaksi(
    val wargaUid: String = "",
    val wargaUsername: String = "",
    val wargaAlamat: String = "",
    val wargaNoTelepon: String = "",
    @ServerTimestamp
    val tanggalDibuat: Date? = null,
    val status: String = "pending",
    val items: List<ItemDraft> = emptyList()
)