package com.bobrovskii.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.ExamStates
import com.bobrovskii.core.toTimestamp
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.usecase.DeleteExamByIdUseCase
import com.bobrovskii.exam.domain.usecase.GetExamsUseCase
import com.bobrovskii.exam.domain.usecase.UpdateExamStateUseCase
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
	private val deleteExamByIdUseCase: DeleteExamByIdUseCase,
	private val updateExamStateUseCase: UpdateExamStateUseCase,
) : ViewModel() {

	@Inject
	lateinit var navigation: HomeNavigation

	private val _exams = MutableStateFlow<List<Exam>>(emptyList())
	val exams: Flow<List<Exam>>
		get() = _exams

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

	//todo: надо добавить в адаптер остальные стейты
	fun getPeriods() {
		viewModelScope.launch {
			try {
				_exams.value = getExamsUseCase()
			} catch (e: HttpException) {
				goBack()
			}
			sortExams()
		}
	}

	// сортировка на разные состояния
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
		//todo: logout сделать
	}

	fun goBack() {
		navigation.goBack()
	}

	fun deleteExam() {
		viewModelScope.launch {
			deleteExamByIdUseCase(examId.value)
			_exams.value = _exams.value.toMutableList().apply { remove(_exams.value.find { it.id == examId.value }) }
		}
	}

	fun openAddExam() {
		navigation.routeToAddExam()
	}

	fun openEditExam(examId: Int) {
		navigation.openEditExamination(examId)
	}

	fun openProgressExam(examId: Int) {
		navigation.routeToProgressExam(examId)
	}

	fun setExamId(examId: Int) {
		_examId.value = examId
	}

	fun changeStateToTimeset(startTime: String) {
		viewModelScope.launch {
			updateExamStateUseCase(examId.value, ExamStates.TIME_SET, startTime.toTimestamp())
		}
	}

	fun changeStateToProgress() {
		viewModelScope.launch {
			updateExamStateUseCase(examId.value, ExamStates.PROGRESS, null)
		}
	}
}