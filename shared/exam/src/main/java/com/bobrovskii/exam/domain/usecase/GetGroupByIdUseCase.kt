package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class GetGroupByIdUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(groupId: Int) =
		repository.getGroupById(groupId)
}