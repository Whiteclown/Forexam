package com.bobrovskii.session.domain.usecase

import com.bobrovskii.session.domain.repository.SessionRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
	private val repository: SessionRepository
) {

	suspend operator fun invoke() {
		repository.clear()
	}
}