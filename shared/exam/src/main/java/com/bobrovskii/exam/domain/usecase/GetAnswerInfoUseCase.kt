package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class GetAnswerInfoUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(answerId: Int) =
		repository.getAnswerInfo(answerId)
}