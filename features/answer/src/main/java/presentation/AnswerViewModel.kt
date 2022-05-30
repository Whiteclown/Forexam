package presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.exam.domain.usecase.GetAnswerInfoUseCase
import com.bobrovskii.exam.domain.usecase.PostMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnswerViewModel @Inject constructor(
	private val getAnswerInfoUseCase: GetAnswerInfoUseCase,
	private val postMessageUseCase: PostMessageUseCase,
	private val router: AnswerRouter,
) : ViewModel() {

	private val _state = MutableStateFlow<AnswerState>(AnswerState.Initial)
	val state = _state.asStateFlow()

	fun loadData(answerId: Int) {
		viewModelScope.launch {
			_state.value = AnswerState.Loading

			_state.value = AnswerState.Content(
				answerInfo = getAnswerInfoUseCase(answerId)
			)
		}
	}

	fun sendMessage(answerId: Int, text: String) {
		viewModelScope.launch {
			_state.value = AnswerState.Loading

			//postMessageUseCase(answerId, text)
			navigateBack()
		}
	}

	fun navigateBack() {
		router.goBack()
	}
}