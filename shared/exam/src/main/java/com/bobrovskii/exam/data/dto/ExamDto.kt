package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExamDto(
	val id: Int,
	//val name: String,
	val examRule: ExamRuleDto,
	val teacher: Teacher,
	val groups: List<GroupDto>,
	//val startTime: String
) {

	@JsonClass(generateAdapter = true)
	data class Teacher(
		val id: Int,
		val account: Account,
		//val discipline: Discipline
	) {

		@JsonClass(generateAdapter = true)
		data class Account(
			val id: Int,
			val username: String,
			//val password: String,
			val name: String,
			val surname: String,
			val roles: List<String>
		)
	}
}

