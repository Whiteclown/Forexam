package com.bobrovskii.exam.domain.entity

data class Message(
	val id: Int,
	val text: String,
	val sendTime: String,
	val senderName: String,
)