package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestExam(
	val discipline: DisciplineDto,
	val examRule: ExamRuleDto,
	val groups: List<GroupDto>,
	val startTime: String
)