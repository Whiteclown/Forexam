package com.bobrovskii.exam.domain.entity

import com.bobrovskii.core.ExamStates

data class Exam(
	val id: Int,
	val name: String,
	val disciplineId: Int,
	val groupId: Int?,
	val oneGroup: Boolean,
	val start: String,
	val end: String,
	val state: ExamStates,
)