package com.sawelo.safacation

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.sawelo.safacation.data.ListDataSafa
import com.sawelo.safacation.data.room.AppDatabase
import com.sawelo.safacation.utils.JsonUtils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SafaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        executorService = Executors.newFixedThreadPool(2)

        val databaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                executorService.execute {
                    val jsonString =
                        JsonUtils.getJsonFromAssets(this@SafaApplication, "source_data.json")
                    val dataSafaList = Gson().fromJson(jsonString, ListDataSafa::class.java).lokasi

                    databaseInstance.DataSafaDao().insertAll(*dataSafaList.toTypedArray())
                }
            }
        }

        databaseInstance = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        )
            .addCallback(databaseCallback)
            .build()
    }

    companion object {
        lateinit var executorService: ExecutorService
        lateinit var databaseInstance: AppDatabase
    }
}