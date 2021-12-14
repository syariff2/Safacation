package com.sawelo.safacation.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sawelo.safacation.data.DataSafa

@Dao
interface DataSafaDao {
    @Query("SELECT * FROM datasafa")
    fun getAll(): LiveData<List<DataSafa>>

    @Query("SELECT * FROM datasafa WHERE nama == :namaLokasi")
    fun getLokasi(namaLokasi: String): LiveData<DataSafa>

    @Insert
    fun insertAll(vararg dataSafa: DataSafa)
}