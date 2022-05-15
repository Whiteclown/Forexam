package com.bobrovskii.exam.domain.usecase

import com.bobrovskii.exam.domain.entity.Ticket
import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class UpdateTicketsRatingUseCase @Inject constructor(
	private val repository: ExamRepository,
) {

	suspend operator fun invoke(ticketsRating: List<Ticket>) =
		repository.updateTicketsRating(ticketsRating)
}