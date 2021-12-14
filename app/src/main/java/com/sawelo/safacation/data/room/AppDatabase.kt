package com.sawelo.safacation.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sawelo.safacation.data.DataSafa
import com.sawelo.safacation.utils.RoomDataConverter

@Database(entities = [DataSafa::class], version = 1)
@TypeConverters(RoomDataConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun DataSafaDao(): DataSafaDao
}