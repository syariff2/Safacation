package com.sawelo.safacation.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class DataSafa(
    @PrimaryKey
    val nama: String,
    val alamat: String,
    val deskripsi: String,
    @SerializedName("jadwal_buka")
    val jadwalBuka: String,
    val bintang: Double,
    var gambar: List<String>,
    var review: MutableList<ReviewLokasi>
)

data class ReviewLokasi(
    @SerializedName("nama_reviewer")
    val namaReviewer: String,
    @SerializedName("konten_reviewer")
    val kontenReviewer: String,
)

data class ListDataSafa(
    val lokasi: List<DataSafa>
)