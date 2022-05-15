package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestTicketsRating(
	val id: Int,
	val semesterRating: Int,
	val examRating: Int,
	val allowed: Boolean,
)
