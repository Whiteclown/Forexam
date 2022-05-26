package com.bobrovskii.exam.domain.entity

data class Exam(
	val id: Int,
	val name: String,
	val disciplineId: Int,
	val groupId: Int?,
	val oneGroup: Boolean,
	val start: String,
	val end: String,
	val state: String,
)