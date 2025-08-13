package com.example.kangpismanapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProfile(
    val email: String = "",
    val username: String = "",
    val namaLengkap: String = "",
    val noTelepon: String = "",
    val alamat: String = "",
    val profileImageUrl: String = "",
    val totalPoin: Int = 0,
    val totalSaldo: Int = 0,
    val totalBeratKg: Double = 0.0
): Parcelable
