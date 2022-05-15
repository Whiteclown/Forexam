package com.bobrovskii.accessexamination.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.domain.usecase.GetStudentByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetUnpassedTicketsUseCase
import com.bobrovskii.exam.domain.usecase.UpdatePeriodStateUseCase
import com.bobrovskii.exam.domain.usecase.UpdateTicketsRatingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccessExamViewModel @Inject constructor(
	private val navigation: AccessExamNavigation,
	private val getUnpassedTicketsUseCase: GetUnpassedTicketsUseCase,
	private val getStudentByIdUseCase: GetStudentByIdUseCase,
	private val updateTicketsRatingUseCase: UpdateTicketsRatingUseCase,
	private val updatePeriodStateUseCase: UpdatePeriodStateUseCase,
) : ViewModel() {

	private val _state = MutableStateFlow<AccessExamState>(AccessExamState.Initial)
	val state = _state.asStateFlow()

	fun loadData(examId: Int) {
		viewModelScope.launch {
			_state.value = AccessExamState.Loading
			val tickets = getUnpassedTicketsUseCase(examId)
			tickets.forEach {
				val student = getStudentByIdUseCase(it.studentId)
				it.studentName = "${student.surname} ${student.name.first()}."
			}
			_state.value = AccessExamState.Content(
				examId = examId,
				tickets = tickets,
			)
		}
	}

	fun saveSemesterRating(ticketId: Int, semesterRating: Int) {
		_state.value.let { content ->
			if (content is AccessExamState.Content) {
				_state.value = content.copy(
					tickets = content.tickets.map {
						if (it.id == ticketId) it.semesterRating = semesterRating
						it
					}
				)
			}
		}
	}

	fun saveAllowance(ticketId: Int, allowance: Boolean) {
		_state.value.let { content ->
			if (content is AccessExamState.Content) {
				_state.value = content.copy(
					tickets = content.tickets.map {
						if (it.id == ticketId) it.allowed = allowance
						it
					}
				)
			}
		}
	}

	fun saveData(isGoingToReady: Boolean) {
		viewModelScope.launch {
			val content = _state.value as AccessExamState.Content
			_state.value = AccessExamState.Loading
			updateTicketsRatingUseCase(content.tickets)
			if (isGoingToReady && content.tickets.isNotEmpty()) {
				updatePeriodStateUseCase(content.tickets[0].examPeriodId, ExamStates.READY)
			}
			navigation.goBack()
		}
	}
}