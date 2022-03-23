package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class GetPeriodsByExamIdUseCase @Inject constructor(
	private val repository: ExamRepository
) {

	suspend operator fun invoke(examId: Int): List<Period> =
		repository.getPeriods(examId = examId)
}