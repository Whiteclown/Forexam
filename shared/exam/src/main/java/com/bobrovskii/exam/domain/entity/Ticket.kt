package com.bobrovskii.exam.domain.entity

data class Ticket(
	val id: Int,
	var semesterRating: Int,
	val examRating: Int,
	var allowed: Boolean,
	val examPeriodId: Int,
	val studentId: Int,
	var studentName: String?,
)
