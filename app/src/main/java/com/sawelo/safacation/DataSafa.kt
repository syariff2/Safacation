package com.sawelo.safacation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataSafa (
    var namelokasi : String,
    var address : String,
    var poster : Int ,
    var deskripsi : String
): Parcelable