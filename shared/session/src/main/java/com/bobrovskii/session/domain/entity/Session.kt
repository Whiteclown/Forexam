package com.bobrovskii.session.domain.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Session(
	val token: String,
	val roles: String
)