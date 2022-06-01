package com.bobrovskii.editexamination.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.domain.usecase.GetDisciplineByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetDisciplinesUseCase
import com.bobrovskii.exam.domain.usecase.GetExamByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetGroupByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetGroupsUseCase
import com.bobrovskii.exam.domain.usecase.UpdateExamStateUseCase
import com.bobrovskii.exam.domain.usecase.UpdateExamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

	fun loadData(examId: Int) {
		viewModelScope.launch {
			_state.value = EditExaminationState.Error
			_state.value = EditExaminationState.Loading
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
		}
	}

	fun updateExam(name: String, discipline: String, choseGroupsId: Int, oneGroup: Boolean, isGoingToReadyState: Boolean) {
		viewModelScope.launch {
			val content = (_state.value as EditExaminationState.Content)
			with(content) {
				disciplines.find { it.name == discipline }?.let {
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
				}
			}
		}
	}

	fun navigateBack() {
		router.goBack()
	}
}