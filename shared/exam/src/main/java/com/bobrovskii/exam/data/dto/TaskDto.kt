package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TaskDto(
	val id: Int,
	val text: String,
	val taskType: String,
)