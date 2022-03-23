package com.bobrovskii.exam.domain.entity

data class ExamRule(
	val id: Int,
	val name: String,
	val questionCount: Int,
	val exerciseCount: Int,
	val duration: Int,
	val minimalRating: Int
)