package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class PostMessageUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(answerId: Int, text: String, artefactId: Int?) =
		repository.postMessage(answerId, text, artefactId)
}