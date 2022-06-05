package com.bobrovskii.exam.data.dto

import com.bobrovskii.core.AnswerStates
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestAnswerRating(
	val id: Int,
	val state: AnswerStates,
	val rating: Int?,
)
