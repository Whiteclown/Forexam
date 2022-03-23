package com.bobrovskii.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.usecase.GetDisciplinesUseCase
import com.bobrovskii.exam.domain.usecase.GetExamRulesByDisciplineUseCase
import com.bobrovskii.exam.domain.usecase.GetGroupsByDisciplineUseCase
import com.bobrovskii.exam.domain.usecase.PostExamUseCase
import com.bobrovskii.home.presentation.navigation.AddExamNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExamViewModel @Inject constructor(
	private val getDisciplinesUseCase: GetDisciplinesUseCase,
	private val getExamRulesByDisciplineUseCase: GetExamRulesByDisciplineUseCase,
	private val getGroupsByDisciplineUseCase: GetGroupsByDisciplineUseCase,
	private val postExamUseCase: PostExamUseCase
) : ViewModel() {

	@Inject
	lateinit var navigation: AddExamNavigation

	private var disciplines = mutableListOf<Discipline>()
	private var examRules = mutableListOf<ExamRule>()
	private var groups = mutableListOf<Group>()

	private val _disciplinesLiveData = MutableLiveData<MutableList<Discipline>>()
	val disciplinesLiveData: LiveData<MutableList<Discipline>>
		get() = _disciplinesLiveData

	private val _examRulesLiveData = MutableLiveData<MutableList<ExamRule>>()
	val examRulesLiveData: LiveData<MutableList<ExamRule>>
		get() = _examRulesLiveData

	private val _groupsLiveData = MutableLiveData<MutableList<Group>>()
	val groupsLiveData: LiveData<MutableList<Group>>
		get() = _groupsLiveData

	init {
		loadDisciplines()
	}

	private fun loadDisciplines() {
		viewModelScope.launch {
			disciplines = getDisciplinesUseCase().toMutableList()
			_disciplinesLiveData.value = disciplines
		}
	}

	fun loadExamRulesAndGroups(localDisciplineId: Int) {
		viewModelScope.launch {
			val disciplineId = disciplines[localDisciplineId].id
			examRules = getExamRulesByDisciplineUseCase(disciplineId).toMutableList()
			_examRulesLiveData.value = examRules

			groups = getGroupsByDisciplineUseCase(disciplineId).toMutableList()
			_groupsLiveData.value = groups
		}
	}

	fun addExam(localDisciplineId: Int, localExamRuleId: Int, choseGroupsIds: List<Int>, startTime: String) {
		viewModelScope.launch {
			postExamUseCase(
				discipline = disciplines[localDisciplineId],
				examRule = examRules[localExamRuleId],
				groups = groups.filter { choseGroupsIds.contains(it.id) },
				startTime = startTime
			)
		}
	}

	fun navigateBack() {
		navigation.goBack()
	}
}