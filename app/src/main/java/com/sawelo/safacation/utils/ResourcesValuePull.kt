package com.sawelo.safacation.utils

import android.content.res.Resources
import com.sawelo.safacation.R
import com.sawelo.safacation.data.DataSafa

class ResourcesValuePull(private val resources: Resources) {
    fun getNamaLokasi(index: Int): String = resources.getStringArray(R.array.nama_lokasi)[index]
    fun getAlamatLokasi(index: Int): String = resources.getStringArray(R.array.alamat_lokasi)[index]
    fun getJadwalBuka(index: Int): String = resources.getStringArray(R.array.jadwal_buka)[index]
    fun getDataBintang(index: Int): Double = resources.getStringArray(R.array.data_bintang)[index].toDouble()

    fun getDataReview(index: Int): List<Pair<String, String>> {
        val dataReviewArray = resources.obtainTypedArray(R.array.data_review)
        val dataReviewArrayId = dataReviewArray.getResourceId(index, 0)
        val dataReviewItemArray = resources.obtainTypedArray(dataReviewArrayId)

        val dataReviewResult = mutableListOf<Pair<String, String>>()

        for (i in 0 until dataReviewItemArray.length()) {
            val arrayId = dataReviewItemArray.getResourceId(i, 0)
            val arrayItem = resources.getStringArray(arrayId)
            dataReviewResult.add(i, Pair(arrayItem.first(), arrayItem.last()))
        }

        dataReviewArray.recycle()
        dataReviewItemArray.recycle()
        return dataReviewResult
    }

    fun getDataPoster(index: Int): List<Int> {
        val dataPosterArray = resources.obtainTypedArray(R.array.data_poster)
        val dataPosterArrayId = dataPosterArray.getResourceId(index, 0)
        val dataPosterItemArray = resources.obtainTypedArray(dataPosterArrayId)

        val dataPosterResult = mutableListOf<Int>()

        for (i in 0 until dataPosterItemArray.length()) {
            val value = dataPosterItemArray.getResourceId(i, 0)
            dataPosterResult.add(i, value)
        }

        dataPosterArray.recycle()
        dataPosterItemArray.recycle()
        return dataPosterResult
    }

    private fun getSpecificDataPoster(dataIndex: Int, specificPosterIndex: Int = 0): Int {
        val dataPosterArray = resources.obtainTypedArray(R.array.data_poster)
        val dataPosterArrayId = dataPosterArray.getResourceId(dataIndex, 0)
        val dataPosterItemArray = resources.obtainTypedArray(dataPosterArrayId)
        val dataPosterResult = dataPosterItemArray.getResourceId(specificPosterIndex, 0)

        dataPosterArray.recycle()
        dataPosterItemArray.recycle()
        return dataPosterResult
    }

    fun getAllDataSafa(): List<DataSafa> {
        val listSafa = mutableListOf<DataSafa>()

        val daftarLokasi = resources.getStringArray(R.array.nama_lokasi)
        for (i in daftarLokasi.indices) {
            listSafa.add(
                DataSafa(
                    resources.getStringArray(R.array.nama_lokasi)[i],
                    resources.getStringArray(R.array.alamat_lokasi)[i],
                    getSpecificDataPoster(i),
                    resources.getStringArray(R.array.deskripsi_lokasi)[i]
                )
            )
        }

        return listSafa
    }
}