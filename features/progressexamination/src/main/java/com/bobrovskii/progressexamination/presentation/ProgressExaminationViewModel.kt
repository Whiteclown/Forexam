package com.bobrovskii.progressexamination.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.exam.domain.usecase.GetAnswersByExamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressExaminationViewModel @Inject constructor(
	private val getAnswersByExamUseCase: GetAnswersByExamUseCase,
	private val router: ProgressExaminationRouter,
) : ViewModel() {

	private val _state = MutableStateFlow<ProgressExaminationState>(ProgressExaminationState.Initial)
	val state = _state.asStateFlow()

	fun getAnswers(examId: Int) {
		viewModelScope.launch {
			_state.value = ProgressExaminationState.Loading

			var noAnswerAnswers: List<Answer> = emptyList()
			var inProgressAnswers: List<Answer> = emptyList()
			var sentAnswers: List<Answer> = emptyList()
			var checkingAnswers: List<Answer> = emptyList()
			var ratedAnswers: List<Answer> = emptyList()
			var noRatingAnswers: List<Answer> = emptyList()

			val answers = getAnswersByExamUseCase(examId)
			answers.forEach {
				when (it.state) {
					AnswersState.NO_ANSWER   -> noAnswerAnswers = noAnswerAnswers.toMutableList().apply { add(it) }
					AnswersState.IN_PROGRESS -> inProgressAnswers = inProgressAnswers.toMutableList().apply { add(it) }
					AnswersState.SENT        -> sentAnswers = sentAnswers.toMutableList().apply { add(it) }
					AnswersState.CHECKING    -> checkingAnswers = checkingAnswers.toMutableList().apply { add(it) }
					AnswersState.RATED       -> ratedAnswers = ratedAnswers.toMutableList().apply { add(it) }
					AnswersState.NO_RATING   -> noRatingAnswers = noRatingAnswers.toMutableList().apply { add(it) }
				}
			}

			_state.value = ProgressExaminationState.Content(
				noAnswerAnswers = noAnswerAnswers,
				inProgressAnswers = inProgressAnswers,
				sentAnswers = sentAnswers,
				checkingAnswers = checkingAnswers,
				ratedAnswers = ratedAnswers,
				noRatingAnswers = noRatingAnswers,
			)
		}
	}

	fun navigateBack() {
		router.goBack()
	}

	fun navigateToAnswer(answerId: Int) {
		router.routeToAnswer(answerId)
	}
}