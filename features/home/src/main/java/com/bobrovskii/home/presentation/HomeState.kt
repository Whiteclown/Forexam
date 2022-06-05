package com.bobrovskii.home.presentation

import com.bobrovskii.exam.domain.entity.Exam

sealed interface HomeState {
	object Initial : HomeState

	object Loading : HomeState

	data class Content(
		val exams: List<Exam>,
		val examsEdit: List<Exam>,
		val examsTimeset: List<Exam>,
		val examsReady: List<Exam>,
		val examsProgress: List<Exam>,
		val examsFinished: List<Exam>,
		val examsClosed: List<Exam>,
		val examId: Int?,
	) : HomeState
}