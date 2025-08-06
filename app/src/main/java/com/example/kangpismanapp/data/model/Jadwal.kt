package com.example.kangpismanapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Jadwal(
    val uid: String = "", // ID pengguna yang membuat jadwal
    @ServerTimestamp
    val tanggalDibuat: Date? = null, // Kapan jadwal ini dibuat
    val tanggalJemput: Date? = null, // Tanggal pilihan pengguna untuk dijemput
    val status: String = "Diajukan"
)
