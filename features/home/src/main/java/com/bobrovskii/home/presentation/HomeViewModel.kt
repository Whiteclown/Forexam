package com.bobrovskii.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.exam.domain.usecase.DeleteExamByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetDisciplineByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetExamsUseCase
import com.bobrovskii.exam.domain.usecase.GetPeriodsByExamIdUseCase
import com.bobrovskii.exam.domain.usecase.UpdatePeriodStateUseCase
import com.bobrovskii.home.presentation.navigation.HomeNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val getExamsUseCase: GetExamsUseCase,
	private val getPeriodsByExamIdUseCase: GetPeriodsByExamIdUseCase,
	private val getDisciplineByIdUseCase: GetDisciplineByIdUseCase,
	private val deleteExamByIdUseCase: DeleteExamByIdUseCase,
	private val updatePeriodStateUseCase: UpdatePeriodStateUseCase,
) : ViewModel() {

	@Inject
	lateinit var navigation: HomeNavigation

	private var exams = listOf<Exam>()

	private val _periods = MutableStateFlow<List<Period>>(emptyList())
	val periods: Flow<List<Period>>
		get() = _periods

	private val _periodsEdit = MutableStateFlow<List<Period>>(emptyList())
	val periodsEdit: Flow<List<Period>>
		get() = _periodsEdit

	private val _periodsAllowance = MutableStateFlow<List<Period>>(emptyList())
	val periodsAllowance: Flow<List<Period>>
		get() = _periodsAllowance

	private val _periodsReady = MutableStateFlow<List<Period>>(emptyList())
	val periodsReady: Flow<List<Period>>
		get() = _periodsReady

	private val _periodsProgress = MutableStateFlow<List<Period>>(emptyList())
	val periodsProgress: Flow<List<Period>>
		get() = _periodsProgress

	private val _periodsFinished = MutableStateFlow<List<Period>>(emptyList())
	val periodsFinished: Flow<List<Period>>
		get() = _periodsFinished

	private val _periodsClosed = MutableStateFlow<List<Period>>(emptyList())
	val periodsClosed: Flow<List<Period>>
		get() = _periodsClosed

	private val _examId = MutableStateFlow(0)
	val examId = _examId.asStateFlow()

	//todo: надо добавить в адаптер остальные стейты
	fun getPeriods() {
		viewModelScope.launch {
			try {
				exams = getExamsUseCase()
			} catch (e: HttpException) {
				goBack()
			}
			_periods.value = _periods.value.toMutableList().apply { clear() }
			exams.forEach { exam ->
				_periods.value = _periods.value.toMutableList().apply {
					addAll(getPeriodsByExamIdUseCase(exam.id).map {
						it.discipline = "prograMIRovanie"/*exam.disciplineId.toString()*///getDisciplineByIdUseCase(exam.disciplineId).name
						return@map it
					})
				}
			}
			sortPeriods()
		}
	}

	//todo: здесь будет сортировка на разные состояния
	private fun sortPeriods() {
		_periodsEdit.value = _periodsEdit.value.toMutableList().apply { clear() }
		_periodsAllowance.value = _periodsAllowance.value.toMutableList().apply { clear() }
		_periodsReady.value = _periodsReady.value.toMutableList().apply { clear() }
		_periodsProgress.value = _periodsProgress.value.toMutableList().apply { clear() }
		_periodsFinished.value = _periodsFinished.value.toMutableList().apply { clear() }
		_periodsClosed.value = _periodsClosed.value.toMutableList().apply { clear() }
		_periods.value.forEach {
			when (it.state) {
				ExamStates.REDACTION -> _periodsEdit.value = _periodsEdit.value.toMutableList().apply { add(it) }
				ExamStates.ALLOWANCE -> _periodsAllowance.value = _periodsAllowance.value.toMutableList().apply { add(it) }
				ExamStates.READY     ->_periodsReady.value = _periodsReady.value.toMutableList().apply { add(it) }
				ExamStates.PROGRESS  -> _periodsProgress.value = _periodsProgress.value.toMutableList().apply { add(it) }
				ExamStates.FINISHED  ->_periodsFinished.value = _periodsFinished.value.toMutableList().apply { add(it) }
				ExamStates.CLOSED    ->_periodsClosed.value = _periodsClosed.value.toMutableList().apply { add(it) }
				else                 -> throw IllegalStateException("Unknown exam state")
			}
		}
	}

	fun logout() {
		//todo: logout сделать
	}

	fun goBack() {
		navigation.goBack()
	}

	fun deleteExam() {
		viewModelScope.launch {
			deleteExamByIdUseCase(examId.value)
			exams = exams.toMutableList().apply { remove(exams.find { it.id == examId.value }) }
		}
	}

	fun openAddExam() {
		navigation.routeToAddExam()
	}

	fun openEditExam(examId: Int) {
		navigation.openEditExamination(examId)
	}

	fun openAccessExam(examId: Int) {
		navigation.routeToAccessExam(examId)
	}

	fun setExamId(examId: Int) {
		_examId.value = examId
	}

	fun changeStateToProgress() {
		viewModelScope.launch {
			updatePeriodStateUseCase(examId.value, ExamStates.PROGRESS)
		}
	}
}