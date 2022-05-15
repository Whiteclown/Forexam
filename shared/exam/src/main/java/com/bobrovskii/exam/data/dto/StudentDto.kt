package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StudentDto(
	val id: Int,
	val account: AccountDto,
	val groupId: Int,
)

@JsonClass(generateAdapter = true)
data class AccountDto(
	val id: Int,
	val username: String,
	val name: String,
	val surname: String,
	val roles: List<String>,
)