package com.sawelo.safacation.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsService {
    @GET("place/findplacefromtext/json")
    fun findPlaceFromText(
        @Query("input") input: String,
        @Query("fields") fields: String,
        @Query("key") key: String,
        @Query("inputtype") inputType: String = "textquery"
    ) : Call<MapsFindPlaceResponse>
}