package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestExamState(
	val id: Int,
	val start: String?,
	val state: String,
)