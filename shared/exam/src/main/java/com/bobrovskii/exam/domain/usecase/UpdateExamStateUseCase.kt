package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class UpdateExamStateUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(examId: Int, state: ExamStates, startTime: String? = null) =
		repository.updateExamState(examId, state, startTime)
}