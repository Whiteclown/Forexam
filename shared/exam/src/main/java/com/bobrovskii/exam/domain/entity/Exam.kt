package com.bobrovskii.exam.domain.entity

data class Exam(
	val id: Int,
	val examRuleId: Int,
	val disciplineId: Int,
	val groupIds: List<Int>,
)