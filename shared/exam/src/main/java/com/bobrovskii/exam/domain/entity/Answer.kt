package com.bobrovskii.exam.domain.entity

import com.bobrovskii.core.AnswerStates

data class Answer(
	val id: Int,
	val taskId: Int,
	val rating: Int,
	val studentRatingId: Int,
	val number: Int,
	var state: AnswerStates,
	var studentName: String? = null,
	var type: String? = null,
)
