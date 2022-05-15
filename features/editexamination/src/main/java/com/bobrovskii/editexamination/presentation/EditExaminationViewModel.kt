package com.bobrovskii.editexamination.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.ExamStates
import com.bobrovskii.core.toTimestamp
import com.bobrovskii.exam.domain.usecase.GetDisciplineByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetDisciplinesUseCase
import com.bobrovskii.exam.domain.usecase.GetExamByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetExamRuleByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetExamRulesByDisciplineUseCase
import com.bobrovskii.exam.domain.usecase.GetGroupsByDisciplineUseCase
import com.bobrovskii.exam.domain.usecase.GetLastPeriodByExamUseCase
import com.bobrovskii.exam.domain.usecase.UpdateExamUseCase
import com.bobrovskii.exam.domain.usecase.UpdatePeriodStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditExaminationViewModel @Inject constructor(
	private val getDisciplinesUseCase: GetDisciplinesUseCase,
	private val getExamRulesByDisciplineUseCase: GetExamRulesByDisciplineUseCase,
	private val getGroupsByDisciplineUseCase: GetGroupsByDisciplineUseCase,
	private val getExamByIdUseCase: GetExamByIdUseCase,
	private val getDisciplineByIdUseCase: GetDisciplineByIdUseCase,
	private val getExamRuleByIdUseCase: GetExamRuleByIdUseCase,
	private val getLastPeriodByExamUseCase: GetLastPeriodByExamUseCase,
	private val updateExamUseCase: UpdateExamUseCase,
	private val updatePeriodStateUseCase: UpdatePeriodStateUseCase,
) : ViewModel() {

	@Inject
	lateinit var navigation: EditExaminationNavigation

	private val _state = MutableStateFlow<EditExaminationState>(EditExaminationState.Initial)
	val state = _state.asStateFlow()

	fun loadData(examId: Int) {
		viewModelScope.launch {
			_state.value = EditExaminationState.Loading
			val exam = getExamByIdUseCase(examId)
			val period = getLastPeriodByExamUseCase(examId)
			val selectedDiscipline = getDisciplineByIdUseCase(exam.disciplineId)
			val selectedExamRule = getExamRuleByIdUseCase(exam.examRuleId)
			val selectedStartTime = period.start
			val selectedGroups = exam.groupIds

			val disciplines = getDisciplinesUseCase().toMutableList()
			val groups = getGroupsByDisciplineUseCase(selectedDiscipline.id).toMutableList()
			val examRules = getExamRulesByDisciplineUseCase(selectedDiscipline.id).toMutableList()

			_state.value = EditExaminationState.Content(
				exam = exam,
				period = period,
				disciplines = disciplines,
				groups = groups,
				examRules = examRules,
				selectedDiscipline = selectedDiscipline,
				selectedExamRule = selectedExamRule,
				selectedStartTime = selectedStartTime,
				selectedGroups = selectedGroups,
			)
		}
	}

	fun loadExamRulesAndGroups(localDisciplineId: Int) {
		viewModelScope.launch {
			val content = (_state.value as EditExaminationState.Content)
			_state.value = EditExaminationState.Loading
			val selectedDiscipline = content.disciplines[localDisciplineId]
			val examRules = getExamRulesByDisciplineUseCase(selectedDiscipline.id).toMutableList()
			val groups = getGroupsByDisciplineUseCase(selectedDiscipline.id).toMutableList()
			_state.value = content.copy(
				groups = groups,
				examRules = examRules,
				selectedDiscipline = selectedDiscipline,
				selectedGroups = null,
				selectedExamRule = null,
			)
		}
	}

	fun setSelectedExamRule(examRuleId: Int) {
		val content = (_state.value as EditExaminationState.Content)
		val selectedExamRule = content.examRules[examRuleId]
		_state.value = content.copy(selectedExamRule = selectedExamRule)
	}

	fun updateExam(choseGroupsIds: List<Int>, startTime: String, isGoingToAccessState: Boolean) {
		viewModelScope.launch {
			val content = (_state.value as EditExaminationState.Content)
			with(content) {
				if (selectedExamRule != null) {
					updateExamUseCase(
						examId = exam.id,
						discipline = selectedDiscipline,
						examRule = selectedExamRule,
						groups = groups.filter { choseGroupsIds.contains(it.id) },
						startTime = startTime.toTimestamp()
					)
					if (isGoingToAccessState) {
						updatePeriodStateUseCase(period.id, ExamStates.ALLOWANCE)
					}
					navigation.goBack()
				}
			}
		}
	}

	fun navigateBack() {
		navigation.goBack()
	}
}