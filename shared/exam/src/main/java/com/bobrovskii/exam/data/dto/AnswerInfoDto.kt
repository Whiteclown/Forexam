package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnswerInfoDto(
	val answer: AnswerDto,
	val task: FullTaskDto,
	val messages: List<FullMessageDto>,
)

@JsonClass(generateAdapter = true)
data class FullMessageDto(
	val message: MessageDto,
	val account: AccountDto,
)

@JsonClass(generateAdapter = true)
data class MessageDto(
	val id: Int,
	val text: String,
	val sendTime: String,
)
