package com.bobrovskii.exam.domain.entity

data class Period(
	val id: Int,
	val start: String,
	val end: String,
	val examId: Int,
	val state: String,
	var discipline: String?,
)