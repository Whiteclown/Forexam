package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExamRuleDto(
	val id: Int,
	val name: String,
	val themes: List<Theme>?,
	val discipline: DisciplineDto,
	val questionCount: Int,
	val exerciseCount: Int,
	val duration: Int,
	val minimalRating: Int
) {

	@JsonClass(generateAdapter = true)
	data class Theme(
		val id: Int,
		val name: String,
		val discipline: DisciplineDto
	)
}