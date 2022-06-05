package com.bobrovskii.exam.domain.entity

data class Message(
	val id: Int,
	val text: String?,
	val sendTime: String,
	val senderName: String,
	val artefact: Artefact?,
	val accountId: Int?,
)

data class Artefact(
	val id: Int,
	val artefactType: String,
	val fileName: String,
)