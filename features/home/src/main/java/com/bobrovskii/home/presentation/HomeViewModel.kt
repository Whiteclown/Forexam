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

	private val _exams = MutableStateFlow<List<Exam>>(emptyList())

	private val _examsEdit = MutableStateFlow<List<Exam>>(emptyList())
	val examsEdit: Flow<List<Exam>>
		get() = _examsEdit

	private val _examsTimeset = MutableStateFlow<List<Exam>>(emptyList())
	val examsTimeset: Flow<List<Exam>>
		get() = _examsTimeset

	private val _examsReady = MutableStateFlow<List<Exam>>(emptyList())
	val examsReady: Flow<List<Exam>>
		get() = _examsReady

	private val _examsProgress = MutableStateFlow<List<Exam>>(emptyList())
	val examsProgress: Flow<List<Exam>>
		get() = _examsProgress

	private val _examsFinished = MutableStateFlow<List<Exam>>(emptyList())
	val examsFinished: Flow<List<Exam>>
		get() = _examsFinished

	private val _examsClosed = MutableStateFlow<List<Exam>>(emptyList())
	val examsClosed: Flow<List<Exam>>
		get() = _examsClosed

	private val _examId = MutableStateFlow(0)
	val examId = _examId.asStateFlow()

	private val _actions: Channel<HomeAction> = Channel(Channel.BUFFERED)
	val actions: Flow<HomeAction> = _actions.receiveAsFlow()

	fun getExams() {
		viewModelScope.launch {
			try {
				_exams.value = getExamsUseCase()
				sortExams()
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

	private fun sortExams() {
		_examsEdit.value = _examsEdit.value.toMutableList().apply { clear() }
		_examsReady.value = _examsReady.value.toMutableList().apply { clear() }
		_examsTimeset.value = _examsTimeset.value.toMutableList().apply { clear() }
		_examsProgress.value = _examsProgress.value.toMutableList().apply { clear() }
		_examsFinished.value = _examsFinished.value.toMutableList().apply { clear() }
		_examsClosed.value = _examsClosed.value.toMutableList().apply { clear() }
		_exams.value.forEach {
			when (it.state) {
				ExamStates.REDACTION -> _examsEdit.value = _examsEdit.value.toMutableList().apply { add(it) }
				ExamStates.READY     -> _examsReady.value = _examsReady.value.toMutableList().apply { add(it) }
				ExamStates.TIME_SET  -> _examsTimeset.value = _examsTimeset.value.toMutableList().apply { add(it) }
				ExamStates.PROGRESS  -> _examsProgress.value = _examsProgress.value.toMutableList().apply { add(it) }
				ExamStates.FINISHED  -> _examsFinished.value = _examsFinished.value.toMutableList().apply { add(it) }
				ExamStates.CLOSED    -> _examsClosed.value = _examsClosed.value.toMutableList().apply { add(it) }
				else                 -> throw IllegalStateException("Unknown exam state")
			}
		}
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
				deleteExamByIdUseCase(examId.value)
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

	fun openAddExam() {
		router.routeToAddExam()
	}

	fun openEditExam(examId: Int) {
		router.routeToEditExam(examId)
	}

	fun openProgressExam(examId: Int) {
		router.routeToProgressExam(examId)
	}

	fun setExamId(examId: Int) {
		_examId.value = examId
	}

	fun changeStateToTimeset(startTime: String) {
		viewModelScope.launch {
			try {
				updateExamStateUseCase(examId.value, ExamStates.TIME_SET, startTime.toTimestamp())
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
				updateExamStateUseCase(examId.value, ExamStates.PROGRESS, null)
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