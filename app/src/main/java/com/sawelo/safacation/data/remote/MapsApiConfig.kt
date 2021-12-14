package com.sawelo.safacation.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object MapsApiConfig {
    fun getApiService(): MapsService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }
}