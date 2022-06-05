package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageNotification(
	val id: Int,
	val text: String,
	val accountId: Int,
)
