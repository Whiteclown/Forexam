package com.bobrovskii.session.domain.usecase

import com.bobrovskii.session.domain.repository.LoginRepository
import com.bobrovskii.session.domain.repository.SessionRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
	private val loginRepository: LoginRepository,
	private val sessionRepository: SessionRepository,
) {

	suspend operator fun invoke(username: String, password: String) {
		val session = loginRepository.login(username = username, password = password)
		sessionRepository.save(session)
	}
}