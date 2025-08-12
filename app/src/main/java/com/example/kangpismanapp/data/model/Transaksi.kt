package com.example.kangpismanapp.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemTransaksi(
    val namaMaterial: String = "",
    val hargaPerKg: Int = 0,
    var beratKg: Double = 0.0,
    var subtotal: Int = 0
) : Parcelable

@Parcelize
data class Transaksi(
    val uid: String = "",
    @ServerTimestamp
    val tanggal: Date? = null,
    val totalPoin: Int = 0,
    val totalRupiah: Int = 0,
    val petugasNama: String = "",
    val petugasUid: String = "",
    val items: List<ItemTransaksi> = emptyList()
) : Parcelable
