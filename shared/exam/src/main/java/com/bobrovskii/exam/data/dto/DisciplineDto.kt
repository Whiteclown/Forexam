package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DisciplineDto(
	val id: Int,
	val name: String
)