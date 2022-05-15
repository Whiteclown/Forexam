package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExamDto(
	val id: Int,
	val examRuleId: Int,
	val disciplineId: Int,
	val groupIds: List<Int>,
)


/*{
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
*/
