package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class GetAnswersByExamUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(examId: Int) =
		repository.getAnswersByExam(examId)
}