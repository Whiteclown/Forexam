package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class UpdateExamUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(examId: Int, name: String, discipline: Discipline, groupId: Int, oneGroup: Boolean) =
		repository.updateExam(examId, name, discipline, groupId, oneGroup)
}