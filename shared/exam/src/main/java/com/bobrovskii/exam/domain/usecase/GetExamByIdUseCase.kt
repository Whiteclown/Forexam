package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class GetExamByIdUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(examId: Int): Exam =
		repository.getExamById(examId)
}