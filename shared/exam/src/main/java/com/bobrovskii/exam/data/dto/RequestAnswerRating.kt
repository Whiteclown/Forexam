package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestAnswerRating(
	val id: Int,
	val rating: Int,
	val state: String = "RATED"
)
