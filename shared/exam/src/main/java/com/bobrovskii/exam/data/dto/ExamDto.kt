package com.bobrovskii.exam.data.dto

import com.bobrovskii.core.ExamStates
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExamDto(
	val id: Int,
	val name: String,
	val disciplineId: Int,
	val groupId: Int?,
	val oneGroup: Boolean,
	val start: String,
	val end: String,
	val state: ExamStates,
)