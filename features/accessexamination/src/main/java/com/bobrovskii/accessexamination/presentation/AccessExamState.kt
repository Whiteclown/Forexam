package com.bobrovskii.accessexamination.presentation

import com.bobrovskii.exam.domain.entity.Ticket

sealed interface AccessExamState {
	object Initial : AccessExamState

	object Loading : AccessExamState

	data class Content(
		val examId: Int,
		val tickets: List<Ticket>
	) : AccessExamState
}