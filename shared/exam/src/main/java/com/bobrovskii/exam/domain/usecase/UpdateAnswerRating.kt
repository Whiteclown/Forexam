package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.core.AnswerStates
import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class UpdateAnswerRating @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(answerId: Int, state: AnswerStates, rating: Int? = null) =
		repository.updateAnswerRating(answerId, state, rating)
}