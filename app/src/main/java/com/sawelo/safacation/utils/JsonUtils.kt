package com.sawelo.safacation.utils

import android.content.Context

object JsonUtils {
    fun getJsonFromAssets(context: Context, fileName: String): String {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)

        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }
}