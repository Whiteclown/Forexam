package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class GetDisciplinesUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke() = repository.getDisciplines()
}