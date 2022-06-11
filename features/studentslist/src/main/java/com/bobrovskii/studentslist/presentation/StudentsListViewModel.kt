package com.bobrovskii.studentslist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.ExamStates
import com.bobrovskii.core.NoNetworkConnectionException
import com.bobrovskii.core.TaskTypes
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
class StudentsListViewModel @Inject constructor(
	private val getAnswersByExamUseCase: GetAnswersByExamUseCase,
	private val updateExamStateUseCase: UpdateExamStateUseCase,
	private val getExamByIdUseCase: GetExamByIdUseCase,
	private val router: StudentsListRouter,
) : ViewModel() {

	private val _state = MutableStateFlow<StudentsListState>(StudentsListState.Initial)
	val state = _state.asStateFlow()

	private val _actions: Channel<StudentsListAction> = Channel(Channel.BUFFERED)
	val actions: Flow<StudentsListAction> = _actions.receiveAsFlow()

	fun getAnswers(examId: Int) {
		viewModelScope.launch {
			if (state.value is StudentsListState.Initial) {
				_state.value = StudentsListState.Loading

				try {
					val answers = getAnswersByExamUseCase(examId)

					val questions = mutableListOf<Answer>()
					val exercises = mutableListOf<Answer>()

					val students: MutableSet<String> = mutableSetOf()

					answers.forEach {
						it.studentName?.let { it1 -> students.add(it1) }
						if (it.type == TaskTypes.QUESTION) {
							questions.add(it)
						} else if (it.type == TaskTypes.EXERCISE) {
							exercises.add(it)
						}
					}

					questions.sortBy { it.number }
					exercises.sortBy { it.number }

					_state.value = StudentsListState.Content(
						questionAnswers = questions,
						exerciseAnswers = exercises,
						examState = getExamByIdUseCase(examId).state,
						filterStudentRatingId = null,
						filterStudentName = null,
						students = students.sorted(),
					)
				} catch (e: Exception) {
					when (e) {
						is HttpException                -> {
							e.response()?.errorBody()?.let { responseBody ->
								val errorMessage = responseBody.charStream().use { stream ->
									stream.readText()
								}
								_actions.send(StudentsListAction.ShowError(errorMessage))
							} ?: run {
								_actions.send(StudentsListAction.ShowError("Возникла непредвиденная ошибка"))
							}
						}

						is NoNetworkConnectionException -> {
							_actions.send(StudentsListAction.ShowError(e.message))
						}

						else                            -> _actions.send(StudentsListAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
					}
				}
			}
		}
	}

	fun refresh(examId: Int) {
		viewModelScope.launch {
			try {
				val content = state.value as StudentsListState.Content

				val answers = getAnswersByExamUseCase(examId)

				val questions = mutableListOf<Answer>()
				val exercises = mutableListOf<Answer>()

				answers.forEach {
					if (it.type == TaskTypes.QUESTION) {
						questions.add(it)
					} else if (it.type == TaskTypes.EXERCISE) {
						exercises.add(it)
					}
				}

				questions.sortBy { it.number }
				exercises.sortBy { it.number }

				_state.value = content.copy(
					questionAnswers = questions,
					exerciseAnswers = exercises,
				)
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(StudentsListAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(StudentsListAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(StudentsListAction.ShowError(e.message))
					}

					else                            -> _actions.send(StudentsListAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun filterByName(studentName: String) {
		val content = state.value as StudentsListState.Content
		_state.value = content.copy(
			filterStudentName = studentName,
		)
	}

	fun turnFilterOff() {
		val content = state.value as StudentsListState.Content
		_state.value = content.copy(
			filterStudentName = null,
		)
	}

	fun finishExam(examId: Int) {
		viewModelScope.launch {
			val content = state.value as StudentsListState.Content
			_state.value = StudentsListState.Loading
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
							_actions.send(StudentsListAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(StudentsListAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(StudentsListAction.ShowError(e.message))
					}

					else                            -> _actions.send(StudentsListAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun navigateBack() {
		router.goBack()
	}

	fun navigateToAnswer(answerId: Int) {
		val content = state.value as StudentsListState.Content
		router.routeFromStudentsListToAnswer(answerId, content.examState == ExamStates.CLOSED)
	}
}