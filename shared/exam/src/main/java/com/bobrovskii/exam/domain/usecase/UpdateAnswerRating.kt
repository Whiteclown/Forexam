package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class UpdateAnswerRating @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(answerId: Int, rating: Int) =
		repository.updateAnswerRating(answerId, rating)
}