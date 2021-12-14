package com.sawelo.safacation.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sawelo.safacation.data.ReviewLokasi
import java.lang.reflect.Type

class RoomDataConverter {
    private val gson = Gson()
    private val listStringType: Type = object : TypeToken<List<String>>() {}.type
    private val listReviewType: Type = object : TypeToken<List<ReviewLokasi>>() {}.type

    @TypeConverter
    fun fromGambarList(list: List<String>): String =
        gson.toJson(list, listStringType)

    @TypeConverter
    fun toGambarList(list: String): List<String> =
        gson.fromJson(list, listStringType)

    @TypeConverter
    fun fromReviewList(list: List<ReviewLokasi>): String =
        gson.toJson(list, listReviewType)

    @TypeConverter
    fun toReviewList(list: String): List<ReviewLokasi> =
        gson.fromJson(list, listReviewType)
}