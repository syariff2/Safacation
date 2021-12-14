package com.sawelo.safacation.data.remote

import com.google.gson.annotations.SerializedName

data class MapsFindPlacesResponse(

	@field:SerializedName("candidates")
	val candidates: List<CandidatesItem>? = null,
)

data class CandidatesItem(

	@field:SerializedName("geometry")
	val geometry: Geometry,

	@field:SerializedName("place_id")
	val placeId: String
)

data class Location(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
)

data class Geometry(
	@field:SerializedName("location")
	val location: Location
)
