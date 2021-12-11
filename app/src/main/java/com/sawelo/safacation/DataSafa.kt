package com.sawelo.safacation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataSafa (
    val namaLokasi : String,
    val alamat : String,
    val poster : Int ,
    val deskripsi : String,
):Parcelable