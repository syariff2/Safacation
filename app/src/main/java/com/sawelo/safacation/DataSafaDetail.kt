package com.sawelo.safacation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataSafaDetail(
    val namaLokasi: String,
    val address: String,
    val dataSchedule: String,
    val dataReview: String,
    val filmPoster: Int,
    val bintang: Double
):Parcelable

