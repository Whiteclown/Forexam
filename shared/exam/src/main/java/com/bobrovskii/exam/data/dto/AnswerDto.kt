package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnswerDto(
	val id: Int,
	val taskId: Int,
	val rating: Int,
	val studentRatingId: Int,
	val number: Int,
	val state: String,
)