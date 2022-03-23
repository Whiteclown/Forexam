package com.bobrovskii.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.exam.domain.usecase.GetExamsUseCase
import com.bobrovskii.exam.domain.usecase.GetPeriodsByExamIdUseCase
import com.bobrovskii.exam.domain.usecase.PostExamUseCase
import com.bobrovskii.home.presentation.navigation.HomeNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val getExamsUseCase: GetExamsUseCase,
	private val getPeriodsByExamIdUseCase: GetPeriodsByExamIdUseCase,
) : ViewModel() {

	@Inject
	lateinit var navigation: HomeNavigation

	private var exams = mutableListOf<Exam>()
	private var periods = mutableListOf<Period>()

	private val _periodsLiveData = MutableLiveData<MutableList<Period>>()
	val periodsLiveData: LiveData<MutableList<Period>>
		get() = _periodsLiveData

	init {
		getPeriods()
	}

	//todo: надо добавить в адаптер остальные стейты
	fun getPeriods() {
		viewModelScope.launch {
			exams = getExamsUseCase().toMutableList()
			exams.forEach { exam ->
				periods.addAll(getPeriodsByExamIdUseCase(exam.id).map {
					it.discipline = exam.name
					return@map it
				})
			}
			sortPeriods()
		}
	}

	//todo: здесь будет сортировка на разные состояния
	private fun sortPeriods() {
		_periodsLiveData.value = periods
	}

	fun logout() {
		//todo: logout сделать
	}

	fun goBack() {
		navigation.goBack()
	}

	fun openAddExam() {
		navigation.openAddExam()
	}
}