package com.bobrovskii.progressexamination.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.AnswerStates
import com.bobrovskii.core.ExamStates
import com.bobrovskii.core.NoNetworkConnectionException
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.exam.domain.usecase.GetAnswersByExamUseCase
import com.bobrovskii.exam.domain.usecase.GetExamByIdUseCase
import com.bobrovskii.exam.domain.usecase.UpdateExamStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ProgressExaminationViewModel @Inject constructor(
	private val getAnswersByExamUseCase: GetAnswersByExamUseCase,
	private val updateExamStateUseCase: UpdateExamStateUseCase,
	private val getExamByIdUseCase: GetExamByIdUseCase,
	private val router: ProgressExaminationRouter,
) : ViewModel() {

	private val _state = MutableStateFlow<ProgressExaminationState>(ProgressExaminationState.Initial)
	val state = _state.asStateFlow()

	private val _actions: Channel<ProgressExaminationAction> = Channel(Channel.BUFFERED)
	val actions: Flow<ProgressExaminationAction> = _actions.receiveAsFlow()

	fun getAnswers(examId: Int) {
		viewModelScope.launch {
			if (state.value is ProgressExaminationState.Initial) {
				_state.value = ProgressExaminationState.Loading

				try {
					var inProgressAnswers: List<Answer> = emptyList()
					var inProgressCount = 0

					var sentAnswers: List<Answer> = emptyList()
					var sentCount = 0

					var checkingAnswers: List<Answer> = emptyList()
					var checkingCount = 0

					var ratedAnswers: List<Answer> = emptyList()
					var noRatingAnswers: List<Answer> = emptyList()

					val answers = getAnswersByExamUseCase(examId)
					answers.forEach {
						when (it.state) {
							AnswerStates.IN_PROGRESS -> {
								inProgressAnswers = inProgressAnswers.toMutableList().apply { add(it) }
								inProgressCount++
							}

							AnswerStates.SENT        -> {
								sentAnswers = sentAnswers.toMutableList().apply { add(it) }
								sentCount++
							}

							AnswerStates.CHECKING    -> {
								checkingAnswers = checkingAnswers.toMutableList().apply { add(it) }
								checkingCount++
							}

							AnswerStates.RATED       -> ratedAnswers = ratedAnswers.toMutableList().apply { add(it) }

							AnswerStates.NO_RATING   -> noRatingAnswers = noRatingAnswers.toMutableList().apply { add(it) }

							AnswerStates.NO_ANSWER   -> {}
						}
					}
					_state.value = ProgressExaminationState.Content(
						inProgressAnswers = inProgressAnswers,
						sentAnswers = sentAnswers,
						checkingAnswers = checkingAnswers,
						ratedAnswers = ratedAnswers,
						noRatingAnswers = noRatingAnswers,
						examState = getExamByIdUseCase(examId).state,
						inProgressCounter = inProgressCount,
						sentCounter = sentCount,
						checkingCounter = checkingCount,
					)
				} catch (e: Exception) {
					when (e) {
						is HttpException                -> {
							e.response()?.errorBody()?.let { responseBody ->
								val errorMessage = responseBody.charStream().use { stream ->
									stream.readText()
								}
								_actions.send(ProgressExaminationAction.ShowError(errorMessage))
							} ?: run {
								_actions.send(ProgressExaminationAction.ShowError("Возникла непредвиденная ошибка"))
							}
						}

						is NoNetworkConnectionException -> {
							_actions.send(ProgressExaminationAction.ShowError(e.message))
						}

						else                            -> _actions.send(ProgressExaminationAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
					}
				}
			}
		}
	}

	fun refresh(examId: Int) {
		viewModelScope.launch {
			try {
				var inProgressAnswers: List<Answer> = emptyList()
				var inProgressCount = 0

				var sentAnswers: List<Answer> = emptyList()
				var sentCount = 0

				var checkingAnswers: List<Answer> = emptyList()
				var checkingCount = 0

				var ratedAnswers: List<Answer> = emptyList()
				var noRatingAnswers: List<Answer> = emptyList()

				val answers = getAnswersByExamUseCase(examId)
				answers.forEach {
					when (it.state) {
						AnswerStates.IN_PROGRESS -> {
							inProgressAnswers = inProgressAnswers.toMutableList().apply { add(it) }
							inProgressCount++
						}

						AnswerStates.SENT        -> {
							sentAnswers = sentAnswers.toMutableList().apply { add(it) }
							sentCount++
						}

						AnswerStates.CHECKING    -> {
							checkingAnswers = checkingAnswers.toMutableList().apply { add(it) }
							checkingCount++
						}

						AnswerStates.RATED       -> ratedAnswers = ratedAnswers.toMutableList().apply { add(it) }

						AnswerStates.NO_RATING   -> noRatingAnswers = noRatingAnswers.toMutableList().apply { add(it) }

						AnswerStates.NO_ANSWER   -> {}
					}
				}
				_state.value = ProgressExaminationState.Content(
					inProgressAnswers = inProgressAnswers,
					sentAnswers = sentAnswers,
					checkingAnswers = checkingAnswers,
					ratedAnswers = ratedAnswers,
					noRatingAnswers = noRatingAnswers,
					examState = getExamByIdUseCase(examId).state,
					inProgressCounter = inProgressCount,
					sentCounter = sentCount,
					checkingCounter = checkingCount,
				)
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(ProgressExaminationAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(ProgressExaminationAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(ProgressExaminationAction.ShowError(e.message))
					}

					else                            -> _actions.send(ProgressExaminationAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun finishExam(examId: Int) {
		viewModelScope.launch {
			val content = state.value as ProgressExaminationState.Content
			_state.value = ProgressExaminationState.Loading
			try {
				if (content.examState == ExamStates.PROGRESS) {
					updateExamStateUseCase(examId, ExamStates.FINISHED)
				} else {
					updateExamStateUseCase(examId, ExamStates.CLOSED)
				}
				navigateBack()
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(ProgressExaminationAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(ProgressExaminationAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(ProgressExaminationAction.ShowError(e.message))
					}

					else                            -> _actions.send(ProgressExaminationAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun navigateBack() {
		router.goBack()
	}

	fun navigateToAnswer(answerId: Int) {
		router.routeToAnswer(answerId)
	}
}