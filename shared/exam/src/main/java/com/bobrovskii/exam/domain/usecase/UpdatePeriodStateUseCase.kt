package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class UpdatePeriodStateUseCase @Inject constructor(
	private val repository: ExamRepository,
){

	suspend operator fun invoke(periodId: Int, state: String) =
		repository.updatePeriodState(periodId, state)
}