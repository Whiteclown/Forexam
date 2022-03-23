package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class PostExamUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(discipline: Discipline, examRule: ExamRule, groups: List<Group>, startTime: String) {
		repository.postExam(discipline, examRule, groups, startTime)
	}
}