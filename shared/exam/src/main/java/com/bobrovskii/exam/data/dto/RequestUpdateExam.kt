package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestUpdateExam(
	val id: Int,
	val name: String,
	val disciplineId: Int,
	val groupId: Int,
	val oneGroup: Boolean,
)