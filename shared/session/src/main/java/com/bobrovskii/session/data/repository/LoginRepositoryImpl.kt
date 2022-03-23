package com.bobrovskii.session.data.repository

import com.bobrovskii.core.Base64Encoder
import com.bobrovskii.session.data.api.SessionApi
import com.bobrovskii.session.data.mapper.toEntity
import com.bobrovskii.session.domain.entity.Session
import com.bobrovskii.session.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
	private val api: SessionApi
) : LoginRepository {

	override suspend fun login(username: String, password: String): Session {
		val authValue = Base64Encoder.encode(firstValue = username, secondValue = password)
		return api.login(authValue).toEntity()
	}
}