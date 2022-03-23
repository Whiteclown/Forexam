package com.bobrovskii.session.domain.usecase

import com.bobrovskii.session.domain.entity.Session
import com.bobrovskii.session.domain.repository.SessionRepository
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
	private val repository: SessionRepository
) {

	suspend operator fun invoke(): Session = repository.get()
}