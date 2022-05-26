package com.bobrovskii.addexamination.presentation

import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Group

sealed interface AddExamState {

	object Initial : AddExamState

	object Loading : AddExamState

	data class Content(
		val disciplines: List<Discipline>,
		val groups: List<Group>,
	) : AddExamState
}