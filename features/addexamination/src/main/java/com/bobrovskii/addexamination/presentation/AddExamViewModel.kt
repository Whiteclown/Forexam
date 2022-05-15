package com.bobrovskii.addexamination.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.toTimestamp
import com.bobrovskii.exam.domain.usecase.GetDisciplinesUseCase
import com.bobrovskii.exam.domain.usecase.GetExamRulesByDisciplineUseCase
import com.bobrovskii.exam.domain.usecase.GetGroupsByDisciplineUseCase
import com.bobrovskii.exam.domain.usecase.PostExamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExamViewModel @Inject constructor(
	private val getDisciplinesUseCase: GetDisciplinesUseCase,
	private val getExamRulesByDisciplineUseCase: GetExamRulesByDisciplineUseCase,
	private val getGroupsByDisciplineUseCase: GetGroupsByDisciplineUseCase,
	private val postExamUseCase: PostExamUseCase,
	private val router: AddExamRouter,
) : ViewModel() {

	private val _state = MutableStateFlow<AddExamState>(AddExamState.Initial)
	val state = _state.asStateFlow()

	fun loadDisciplines() {
		viewModelScope.launch {
			_state.value = AddExamState.Loading
			val disciplines = getDisciplinesUseCase().toMutableList()
			_state.value = AddExamState.Content(
				disciplines = disciplines,
				groups = null,
				examRules = null,
				selectedDiscipline = null,
				selectedExamRule = null,
				selectedTime = "",
			)
		}
	}

	fun loadExamRulesAndGroups(localDisciplineId: Int) {
		viewModelScope.launch {
			val content = (_state.value as AddExamState.Content)
			content.disciplines?.let {
				_state.value = AddExamState.Loading
				val selectedDiscipline = content.disciplines[localDisciplineId]
				val examRules = getExamRulesByDisciplineUseCase(selectedDiscipline.id).toMutableList()
				val groups = getGroupsByDisciplineUseCase(selectedDiscipline.id).toMutableList()
				_state.value = AddExamState.Content(
					disciplines = content.disciplines,
					groups = groups,
					examRules = examRules,
					selectedDiscipline = selectedDiscipline,
					selectedExamRule = null,
					selectedTime = "",
				)
			}
		}
	}

	fun addExam(choseGroupsIds: List<Int>, editTextStartDate: String) {
		viewModelScope.launch {
			val content = (_state.value as AddExamState.Content)
			with(content) {
				if (selectedDiscipline != null && selectedExamRule != null && groups != null) {
					postExamUseCase(
						discipline = selectedDiscipline,
						examRule = selectedExamRule,
						groups = groups.filter { choseGroupsIds.contains(it.id) },
						startTime = editTextStartDate.toTimestamp()
					)
					router.routeBack()
				}
			}
		}
	}

	fun setSelectedExamRule(examRuleId: Int) {
		val content = (_state.value as AddExamState.Content)
		val selectedExamRule = content.examRules?.get(examRuleId)
		_state.value = content.copy(selectedExamRule = selectedExamRule)
	}

	fun navigateBack() {
		router.routeBack()
	}
}