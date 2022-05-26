package com.bobrovskii.addexamination.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.exam.domain.usecase.GetDisciplinesUseCase
import com.bobrovskii.exam.domain.usecase.GetGroupsUseCase
import com.bobrovskii.exam.domain.usecase.PostExamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExamViewModel @Inject constructor(
	private val getDisciplinesUseCase: GetDisciplinesUseCase,
	private val getGroupsUseCase: GetGroupsUseCase,
	private val postExamUseCase: PostExamUseCase,
	private val router: AddExamRouter,
) : ViewModel() {

	private val _state = MutableStateFlow<AddExamState>(AddExamState.Initial)
	val state = _state.asStateFlow()

	fun loadData() {
		viewModelScope.launch {
			_state.value = AddExamState.Loading
			val disciplines = getDisciplinesUseCase().toMutableList()
			val groups = getGroupsUseCase().toMutableList()
			_state.value = AddExamState.Content(
				disciplines = disciplines,
				groups = groups,
			)
		}
	}

	fun addExam(name: String, discipline: String, choseGroupId: Int, oneGroup: Boolean) {
		viewModelScope.launch {
			val content = (_state.value as AddExamState.Content)
			content.disciplines.find { it.name == discipline }?.let {
				postExamUseCase(
					name = name,
					discipline = it,
					groupId = if (oneGroup) choseGroupId else -1,
					oneGroup = oneGroup,
				)
				router.routeBack()
			}
		}
	}

	fun navigateBack() {
		router.routeBack()
	}
}