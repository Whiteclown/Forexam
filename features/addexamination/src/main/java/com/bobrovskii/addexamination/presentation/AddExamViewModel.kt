package com.bobrovskii.addexamination.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.NoNetworkConnectionException
import com.bobrovskii.exam.domain.usecase.GetDisciplinesUseCase
import com.bobrovskii.exam.domain.usecase.GetGroupsUseCase
import com.bobrovskii.exam.domain.usecase.PostExamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

	private val _actions: Channel<AddExamAction> = Channel(Channel.BUFFERED)
	val actions: Flow<AddExamAction> = _actions.receiveAsFlow()

	fun loadData() {
		viewModelScope.launch {
			_state.map { }
			state.map { }
			_state.value = AddExamState.Loading
			try {
				val disciplines = getDisciplinesUseCase().toMutableList()
				val groups = getGroupsUseCase().toMutableList()
				_state.value = AddExamState.Content(
					disciplines = disciplines,
					groups = groups,
				)
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(AddExamAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(AddExamAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(AddExamAction.ShowError(e.message))
					}

					else                            -> _actions.send(AddExamAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun addExam(name: String, discipline: String, choseGroupId: Int, oneGroup: Boolean) {
		viewModelScope.launch {
			val content = (_state.value as AddExamState.Content)
			content.disciplines.find { it.name == discipline }?.let {
				try {
					postExamUseCase(
						name = name,
						discipline = it,
						groupId = if (oneGroup) choseGroupId else -1,
						oneGroup = oneGroup,
					)
					router.routeBack()
				} catch (e: Exception) {
					when (e) {
						is HttpException                -> {
							e.response()?.errorBody()?.let { responseBody ->
								val errorMessage = responseBody.charStream().use { stream ->
									stream.readText()
								}
								_actions.send(AddExamAction.ShowError(errorMessage))
							} ?: run {
								_actions.send(AddExamAction.ShowError("Возникла непредвиденная ошибка"))
							}
						}

						is NoNetworkConnectionException -> {
							_actions.send(AddExamAction.ShowError(e.message))
						}

						else                            -> _actions.send(AddExamAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
					}
				}
			}
		}
	}

	fun navigateBack() {
		router.routeBack()
	}
}