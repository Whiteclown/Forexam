package com.bobrovskii.editexamination.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.ExamStates
import com.bobrovskii.core.NoNetworkConnectionException
import com.bobrovskii.exam.domain.usecase.GetDisciplineByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetDisciplinesUseCase
import com.bobrovskii.exam.domain.usecase.GetExamByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetGroupByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetGroupsUseCase
import com.bobrovskii.exam.domain.usecase.UpdateExamStateUseCase
import com.bobrovskii.exam.domain.usecase.UpdateExamUseCase
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
class EditExaminationViewModel @Inject constructor(
	private val getDisciplinesUseCase: GetDisciplinesUseCase,
	private val getGroupsUseCase: GetGroupsUseCase,
	private val getExamByIdUseCase: GetExamByIdUseCase,
	private val getDisciplineByIdUseCase: GetDisciplineByIdUseCase,
	private val updateExamUseCase: UpdateExamUseCase,
	private val updateExamStateUseCase: UpdateExamStateUseCase,
	private val getGroupByIdUseCase: GetGroupByIdUseCase,
	private val router: EditExaminationRouter,
) : ViewModel() {

	private val _state = MutableStateFlow<EditExaminationState>(EditExaminationState.Initial)
	val state = _state.asStateFlow()

	private val _actions: Channel<EditExaminationAction> = Channel(Channel.BUFFERED)
	val actions: Flow<EditExaminationAction> = _actions.receiveAsFlow()

	fun loadData(examId: Int) {
		viewModelScope.launch {
			_state.value = EditExaminationState.Loading
			try {
				val exam = getExamByIdUseCase(examId)
				val selectedDiscipline = getDisciplineByIdUseCase(exam.disciplineId)
				val selectedGroup = exam.groupId?.let { getGroupByIdUseCase(it) }
				val disciplines = getDisciplinesUseCase().toMutableList()
				val groups = getGroupsUseCase().toMutableList()

				_state.value = EditExaminationState.Content(
					exam = exam,
					disciplines = disciplines,
					groups = groups,
					selectedDiscipline = selectedDiscipline,
					selectedGroup = selectedGroup,
				)
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(EditExaminationAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(EditExaminationAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(EditExaminationAction.ShowError(e.message))
					}

					else                            -> _actions.send(EditExaminationAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun updateExam(name: String, discipline: String, choseGroupsId: Int, oneGroup: Boolean, isGoingToReadyState: Boolean) {
		viewModelScope.launch {
			val content = (_state.value as EditExaminationState.Content)
			with(content) {
				disciplines.find { it.name == discipline }?.let {
					try {
						updateExamUseCase(
							examId = exam.id,
							name = name,
							discipline = it,
							groupId = choseGroupsId,
							oneGroup = oneGroup,
						)
						if (isGoingToReadyState) {
							updateExamStateUseCase(exam.id, ExamStates.READY, null)
						}
						navigateBack()
					} catch (e: Exception) {
						when (e) {
							is HttpException                -> {
								e.response()?.errorBody()?.let { responseBody ->
									val errorMessage = responseBody.charStream().use { stream ->
										stream.readText()
									}
									_actions.send(EditExaminationAction.ShowError(errorMessage))
								} ?: run {
									_actions.send(EditExaminationAction.ShowError("Возникла непредвиденная ошибка"))
								}
							}

							is NoNetworkConnectionException -> {
								_actions.send(EditExaminationAction.ShowError(e.message))
							}

							else                            -> _actions.send(EditExaminationAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
						}
					}
				}
			}
		}
	}

	fun navigateBack() {
		router.goBack()
	}
}