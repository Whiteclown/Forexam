package com.bobrovskii.session.data.api

import com.bobrovskii.core.Headers
import com.bobrovskii.session.data.dto.SessionDto
import retrofit2.http.Header
import retrofit2.http.POST

interface SessionApi {

	@POST("/login")
	suspend fun login(@Header(Headers.XAuthentication) auth: String): SessionDto
}