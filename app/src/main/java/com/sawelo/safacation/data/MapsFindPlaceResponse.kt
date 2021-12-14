package com.sawelo.safacation.data

import com.google.gson.annotations.SerializedName

data class MapsFindPlaceResponse(

	@field:SerializedName("candidates")
	val candidates: List<CandidatesItem>? = null,
)

data class CandidatesItem(

	@field:SerializedName("geometry")
	val geometry: Geometry
)

data class Geometry(

	@field:SerializedName("location")
	val location: Location
)

data class Location(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
)