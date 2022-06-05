package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestMessage(
	val text: String,
	val artefactId: Int?,
)
