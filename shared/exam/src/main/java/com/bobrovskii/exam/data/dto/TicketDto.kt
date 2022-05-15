package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TicketDto(
	val id: Int,
	val semesterRating: Int,
	val examRating: Int,
	val allowed: Boolean,
	val examPeriodId: Int,
	val studentId: Int,
)