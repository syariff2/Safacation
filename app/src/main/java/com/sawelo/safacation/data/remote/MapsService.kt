package com.sawelo.safacation.data.remote

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
    ): Call<MapsFindPlacesResponse>

    @GET("place/details/json")
    fun getDetails(
        @Query("place_id") placeId: String,
        @Query("fields") fields: String,
        @Query("key") key: String,
        @Query("language") language: String = "id"
    ): Call<MapsDetailsReviewResponse>
}