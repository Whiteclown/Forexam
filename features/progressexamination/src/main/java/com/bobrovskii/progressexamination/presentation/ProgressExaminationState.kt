package com.bobrovskii.progressexamination.presentation

import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.domain.entity.Answer

sealed interface ProgressExaminationState {
	object Initial : ProgressExaminationState

	object Loading : ProgressExaminationState

	data class Content(
		val noAnswerAnswers: List<Answer>,
		val inProgressAnswers: List<Answer>,
		val sentAnswers: List<Answer>,
		val checkingAnswers: List<Answer>,
		val ratedAnswers: List<Answer>,
		val noRatingAnswers: List<Answer>,
		val examState: ExamStates,
	) : ProgressExaminationState
}