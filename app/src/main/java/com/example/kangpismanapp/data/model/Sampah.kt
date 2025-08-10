package com.example.kangpismanapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sampah(
    val id: Int = 0,
    val nama: String = "",
    val harga: Int = 0,
    val imageUrl: String = ""
) : Parcelable
