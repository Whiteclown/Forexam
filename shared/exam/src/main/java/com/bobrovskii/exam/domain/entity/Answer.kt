package com.bobrovskii.exam.domain.entity

data class Answer(
	val id: Int,
	val taskId: Int,
	val rating: Int,
	val studentRatingId: Int,
	val number: Int,
	val state: String,
	var studentName: String? = null,
	var type: String? = null,
)
