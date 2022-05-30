package presentation

import com.bobrovskii.exam.domain.entity.AnswerInfo

sealed interface AnswerState {
	object Initial : AnswerState

	object Loading : AnswerState

	data class Content(
		val answerInfo: AnswerInfo,
	) : AnswerState
}