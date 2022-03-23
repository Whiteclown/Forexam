package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class GetExamRulesByDisciplineUseCase @Inject constructor(
	private val repository: ExamRepository
) {

	suspend operator fun invoke(disciplineId: Int) =
		repository.getExamRulesByDiscipline(disciplineId)
}