package com.bobrovskii.editexamination.presentation

import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.entity.Period

sealed interface EditExaminationState {

	object Initial : EditExaminationState

	object Loading : EditExaminationState

	data class Content(
		val exam: Exam,
		val period: Period,
		val disciplines: List<Discipline>,
		val groups: List<Group>,
		val examRules: List<ExamRule>,
		val selectedDiscipline: Discipline,
		val selectedExamRule: ExamRule?,
		val selectedStartTime: String,
		val selectedGroups: List<Int>?,
	) : EditExaminationState
}