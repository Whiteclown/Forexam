package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class UpdateExamUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(examId: Int, discipline: Discipline, examRule: ExamRule, groups: List<Group>, startTime: String) =
		repository.updateExam(examId, discipline, examRule, groups, startTime)
}