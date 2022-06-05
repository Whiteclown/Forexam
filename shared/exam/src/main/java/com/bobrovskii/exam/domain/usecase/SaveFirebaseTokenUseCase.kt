package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class SaveFirebaseTokenUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(token: String) =
		repository.saveFirebaseToken(token)
}