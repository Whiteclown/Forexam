package com.bobrovskii.editexamination.presentation

import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.Group

sealed interface EditExaminationState {
	object Initial : EditExaminationState

	object Loading : EditExaminationState

	data class Content(
		val exam: Exam,
		val disciplines: List<Discipline>,
		val groups: List<Group>,
		val selectedDiscipline: Discipline,
		val selectedGroup: Group?,
	) : EditExaminationState
}