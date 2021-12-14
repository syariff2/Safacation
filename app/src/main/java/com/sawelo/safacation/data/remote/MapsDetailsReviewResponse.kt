package com.sawelo.safacation.data.remote

import com.google.gson.annotations.SerializedName

data class MapsDetailsReviewResponse(

	@field:SerializedName("result")
	val result: Result? = null,
)

data class ReviewsItem(

	@field:SerializedName("author_name")
	val authorName: String,

	@field:SerializedName("text")
	val text: String,
)

data class Result(

	@field:SerializedName("reviews")
	val reviews: List<ReviewsItem>
)
