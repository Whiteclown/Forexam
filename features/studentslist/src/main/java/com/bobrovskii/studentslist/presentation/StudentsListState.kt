package com.bobrovskii.studentslist.presentation

import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.domain.entity.Answer

sealed interface StudentsListState {
	object Initial : StudentsListState

	object Loading : StudentsListState

	data class Content(
		val questionAnswers: List<Answer>,
		val exerciseAnswers: List<Answer>,
		val examState: ExamStates,
		val filterStudentRatingId: Int?,
		val filterStudentName: String?,
		val students: List<String>?,
	) : StudentsListState
}