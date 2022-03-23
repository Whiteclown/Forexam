package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PeriodDto(
	val id: Int,
	val start: String,
	val end: String,
	//val exam: ExamDto,
	val state: String
)