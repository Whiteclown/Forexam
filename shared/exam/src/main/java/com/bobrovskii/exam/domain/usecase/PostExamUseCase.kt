package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class PostExamUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(name: String, discipline: Discipline, groupId: Int, oneGroup: Boolean) {
		repository.postExam(name, discipline, groupId, oneGroup)
	}
}