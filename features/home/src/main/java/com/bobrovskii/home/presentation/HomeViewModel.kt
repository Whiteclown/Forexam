package com.bobrovskii.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.ExamStates
import com.bobrovskii.core.NoNetworkConnectionException
import com.bobrovskii.core.toTimestamp
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.usecase.DeleteExamByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetExamsUseCase
import com.bobrovskii.exam.domain.usecase.UpdateExamStateUseCase
import com.bobrovskii.session.domain.usecase.LogoutUseCase
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
class HomeViewModel @Inject constructor(
	private val getExamsUseCase: GetExamsUseCase,
	private val deleteExamByIdUseCase: DeleteExamByIdUseCase,
	private val updateExamStateUseCase: UpdateExamStateUseCase,
	private val logoutUseCase: LogoutUseCase,
	private val router: HomeRouter,
) : ViewModel() {

	private val _state = MutableStateFlow<HomeState>(HomeState.Initial)
	val state = _state.asStateFlow()

	private val _actions: Channel<HomeAction> = Channel(Channel.BUFFERED)
	val actions: Flow<HomeAction> = _actions.receiveAsFlow()

	fun getExams() {
		viewModelScope.launch {
			try {
				_state.value = HomeState.Loading
				val exams = getExamsUseCase()
				sortExams(exams)
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						if (e.code() == 401) {
							routeToSignIn()
						} else {
							e.response()?.errorBody()?.let { responseBody ->
								val errorMessage = responseBody.charStream().use { stream ->
									stream.readText()
								}
								_actions.send(HomeAction.ShowError(errorMessage))
							} ?: run {
								_actions.send(HomeAction.ShowError("Возникла непредвиденная ошибка"))
							}
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(HomeAction.ShowError(e.message))
					}

					else                            -> _actions.send(HomeAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	private fun sortExams(exams: List<Exam>) {
		val examsEdit = mutableListOf<Exam>()
		val examsReady = mutableListOf<Exam>()
		val examsTimeset = mutableListOf<Exam>()
		val examsProgress = mutableListOf<Exam>()
		val examsFinished = mutableListOf<Exam>()
		val examsClosed = mutableListOf<Exam>()
		exams.forEach {
			when (it.state) {
				ExamStates.REDACTION -> examsEdit.add(it)
				ExamStates.READY     -> examsReady.add(it)
				ExamStates.TIME_SET  -> examsTimeset.add(it)
				ExamStates.PROGRESS  -> examsProgress.add(it)
				ExamStates.FINISHED  -> examsFinished.add(it)
				ExamStates.CLOSED    -> examsClosed.add(it)
				else                 -> throw IllegalStateException("Unknown exam state")
			}
		}
		_state.value = HomeState.Content(
			exams = exams,
			examsEdit = examsEdit,
			examsTimeset = examsTimeset,
			examsReady = examsReady,
			examsProgress = examsProgress,
			examsFinished = examsFinished,
			examsClosed = examsClosed,
			examId = null,
		)
	}

	fun logout() {
		viewModelScope.launch {
			logoutUseCase()
			routeToSignIn()
		}
	}

	private fun routeToSignIn() {
		router.routeToSignIn()
	}

	fun deleteExam() {
		viewModelScope.launch {
			try {
				if (state.value is HomeState.Content) {
					(state.value as HomeState.Content).examId?.let {
						deleteExamByIdUseCase(it)
						getExams()
					}
				}
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(HomeAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(HomeAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(HomeAction.ShowError(e.message))
					}

					else                            -> _actions.send(HomeAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun openAddExam() {
		router.routeToAddExam()
	}

	fun openEditExam(examId: Int) {
		router.routeToEditExam(examId)
	}

	fun openAnswersList(examId: Int) {
		router.routeToAnswersList(examId)
	}

	fun setExamId(examId: Int) {
		if (state.value is HomeState.Content) {
			_state.value = (state.value as HomeState.Content).copy(
				examId = examId,
			)
		}
	}

	fun changeStateToTimeset(startTime: String) {
		viewModelScope.launch {
			try {
				if (state.value is HomeState.Content) {
					(state.value as HomeState.Content).examId?.let {
						updateExamStateUseCase(it, ExamStates.TIME_SET, startTime.toTimestamp())
						getExams()
					}
				}
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(HomeAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(HomeAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(HomeAction.ShowError(e.message))
					}

					else                            -> _actions.send(HomeAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun restoreFromTimeset(examId: Int) {
		viewModelScope.launch {
			try {
				updateExamStateUseCase(examId, ExamStates.READY)
				getExams()
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(HomeAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(HomeAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(HomeAction.ShowError(e.message))
					}

					else                            -> _actions.send(HomeAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun changeStateToProgress() {
		viewModelScope.launch {
			try {
				if (state.value is HomeState.Content) {
					(state.value as HomeState.Content).examId?.let {
						updateExamStateUseCase(it, ExamStates.PROGRESS, null)
						getExams()
					}
				}
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(HomeAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(HomeAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(HomeAction.ShowError(e.message))
					}

					else                            -> _actions.send(HomeAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun restoreFromFinished(examId: Int) {
		viewModelScope.launch {
			try {
				updateExamStateUseCase(examId, ExamStates.PROGRESS)
				getExams()
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(HomeAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(HomeAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(HomeAction.ShowError(e.message))
					}

					else                            -> _actions.send(HomeAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}
}