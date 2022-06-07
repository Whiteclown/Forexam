package com.bobrovskii.answerslist.presentation

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
class AnswersListViewModel @Inject constructor(
	private val getAnswersByExamUseCase: GetAnswersByExamUseCase,
	private val updateExamStateUseCase: UpdateExamStateUseCase,
	private val getExamByIdUseCase: GetExamByIdUseCase,
	private val router: AnswersListRouter,
) : ViewModel() {

	private val _state = MutableStateFlow<AnswersListState>(AnswersListState.Initial)
	val state = _state.asStateFlow()

	private val _actions: Channel<AnswersListAction> = Channel(Channel.BUFFERED)
	val actions: Flow<AnswersListAction> = _actions.receiveAsFlow()

	fun getAnswers(examId: Int) {
		viewModelScope.launch {
			if (state.value is AnswersListState.Initial) {
				_state.value = AnswersListState.Loading

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
					_state.value = AnswersListState.Content(
						inProgressAnswers = inProgressAnswers,
						sentAnswers = sentAnswers,
						checkingAnswers = checkingAnswers,
						ratedAnswers = ratedAnswers,
						noRatingAnswers = noRatingAnswers,
						examState = getExamByIdUseCase(examId).state,
						inProgressCounter = inProgressCount,
						sentCounter = sentCount,
						checkingCounter = checkingCount,
						filterStudentRatingId = null,
						filterStudentName = null,
					)
				} catch (e: Exception) {
					when (e) {
						is HttpException                -> {
							e.response()?.errorBody()?.let { responseBody ->
								val errorMessage = responseBody.charStream().use { stream ->
									stream.readText()
								}
								_actions.send(AnswersListAction.ShowError(errorMessage))
							} ?: run {
								_actions.send(AnswersListAction.ShowError("Возникла непредвиденная ошибка"))
							}
						}

						is NoNetworkConnectionException -> {
							_actions.send(AnswersListAction.ShowError(e.message))
						}

						else                            -> _actions.send(AnswersListAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
					}
				}
			}
		}
	}

	fun refresh(examId: Int) {
		viewModelScope.launch {
			try {
				val content = state.value as AnswersListState.Content
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
				_state.value = content.copy(
					inProgressAnswers = inProgressAnswers,
					sentAnswers = sentAnswers,
					checkingAnswers = checkingAnswers,
					ratedAnswers = ratedAnswers,
					noRatingAnswers = noRatingAnswers,
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
							_actions.send(AnswersListAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(AnswersListAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(AnswersListAction.ShowError(e.message))
					}

					else                            -> _actions.send(AnswersListAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun turnFilterOn(studentRatingId: Int, studentName: String) {
		val content = state.value as AnswersListState.Content
		_state.value = content.copy(
			filterStudentRatingId = studentRatingId,
			filterStudentName = studentName,
		)
	}

	fun turnFilterOff() {
		val content = state.value as AnswersListState.Content
		_state.value = content.copy(
			filterStudentRatingId = null,
			filterStudentName = null,
		)
	}

	fun finishExam(examId: Int) {
		viewModelScope.launch {
			val content = state.value as AnswersListState.Content
			_state.value = AnswersListState.Loading
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
							_actions.send(AnswersListAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(AnswersListAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(AnswersListAction.ShowError(e.message))
					}

					else                            -> _actions.send(AnswersListAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
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