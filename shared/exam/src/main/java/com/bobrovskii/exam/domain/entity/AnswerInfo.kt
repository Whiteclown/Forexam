package com.bobrovskii.exam.domain.entity

data class AnswerInfo(
	val answer: Answer,
	val task: Task,
	val messages: List<Message>,
)