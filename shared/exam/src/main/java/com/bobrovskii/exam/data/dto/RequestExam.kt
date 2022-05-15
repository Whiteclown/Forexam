package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestExam(
	val examRuleId: Int,
	val disciplineId: Int,
	val groupIds: List<Int>,
	val startTime: String,
)