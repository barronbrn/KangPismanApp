package com.example.kangpismanapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artikel(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val content: String = ""
): Parcelable
