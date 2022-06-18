package com.bobrovskii.exam.domain.entity

data class Ticket(
	val answers: MutableList<Answer>,
	var studentName: String,
	var semesterRating: Int,
	var examRating: Int,
)
