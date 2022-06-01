package com.bobrovskii.session.data.interceptor

import com.bobrovskii.core.Headers
import com.bobrovskii.session.domain.repository.SessionRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class SessionInterceptor @Inject constructor(
	private val sessionRepository: SessionRepository
) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val token = sessionRepository.get().token

		val authRequest = request
			.newBuilder()
			.addHeader(Headers.XAccessToken, token)
			.build()

		return chain.proceed(authRequest)
	}
}