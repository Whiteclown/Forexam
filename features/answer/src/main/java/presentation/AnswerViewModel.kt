package presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.artefact.domain.usecase.GetArtefactMetaDataUseCase
import com.bobrovskii.artefact.domain.usecase.GetArtefactUseCase
import com.bobrovskii.artefact.domain.usecase.PostArtefactUseCase
import com.bobrovskii.core.AnswerStates
import com.bobrovskii.core.NoNetworkConnectionException
import com.bobrovskii.exam.domain.usecase.GetAnswerInfoUseCase
import com.bobrovskii.exam.domain.usecase.PostMessageUseCase
import com.bobrovskii.exam.domain.usecase.UpdateAnswerRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AnswerViewModel @Inject constructor(
	private val getAnswerInfoUseCase: GetAnswerInfoUseCase,
	private val postMessageUseCase: PostMessageUseCase,
	private val updateAnswerRating: UpdateAnswerRating,
	private val getArtefactUseCase: GetArtefactUseCase,
	private val getArtefactMetaDataUseCase: GetArtefactMetaDataUseCase,
	private val postArtefactUseCase: PostArtefactUseCase,
	private val router: AnswerRouter,
) : ViewModel() {

	private val _state = MutableStateFlow<AnswerState>(AnswerState.Initial)
	val state = _state.asStateFlow()

	private val _actions: Channel<AnswerAction> = Channel(Channel.BUFFERED)
	val actions: Flow<AnswerAction> = _actions.receiveAsFlow()

	fun loadData(answerId: Int) {
		viewModelScope.launch {
			_state.value = AnswerState.Loading

			try {
				val answerInfo = getAnswerInfoUseCase(answerId)
				if (answerInfo.answer.state == AnswerStates.SENT) {
					checkAnswer(answerId)
					answerInfo.answer.state = AnswerStates.CHECKING
				}

				_state.value = AnswerState.Content(
					answerInfo = answerInfo,
					metaData = null,
					file = null,
				)
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(AnswerAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(AnswerAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(AnswerAction.ShowError(e.message))
					}

					else                            -> _actions.send(AnswerAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun refresh(answerId: Int, accountId: Int) {
		if (_state.value is AnswerState.Content) {
			viewModelScope.launch {
				try {
					if ((_state.value as AnswerState.Content).answerInfo.messages.isNotEmpty() &&
						(_state.value as AnswerState.Content).answerInfo.messages.first().accountId == accountId
					) {
						val answerInfo = getAnswerInfoUseCase(answerId)

						_state.value = AnswerState.Content(
							answerInfo = answerInfo,
							metaData = null,
							file = null,
						)
					}
				} catch (e: Exception) {
					when (e) {
						is HttpException                -> {
							e.response()?.errorBody()?.let { responseBody ->
								val errorMessage = responseBody.charStream().use { stream ->
									stream.readText()
								}
								_actions.send(AnswerAction.ShowError(errorMessage))
							} ?: run {
								_actions.send(AnswerAction.ShowError("Возникла непредвиденная ошибка"))
							}
						}

						is NoNetworkConnectionException -> {
							_actions.send(AnswerAction.ShowError(e.message))
						}

						else                            -> _actions.send(AnswerAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
					}
				}
			}
		}
	}

	fun sendMessage(answerId: Int, text: String) {
		viewModelScope.launch {
			if (state.value is AnswerState.Content) {
				val content = state.value as AnswerState.Content
				try {
					postMessageUseCase(answerId, text, content.metaData?.id)
					_state.value = content.copy(
						metaData = null,
					)
					loadData(answerId)
				} catch (e: Exception) {
					when (e) {
						is HttpException                -> {
							e.response()?.errorBody()?.let { responseBody ->
								val errorMessage = responseBody.charStream().use { stream ->
									stream.readText()
								}
								_actions.send(AnswerAction.ShowError(errorMessage))
							} ?: run {
								_actions.send(AnswerAction.ShowError("Возникла непредвиденная ошибка"))
							}
						}

						is NoNetworkConnectionException -> {
							_actions.send(AnswerAction.ShowError(e.message))
						}

						else                            -> _actions.send(AnswerAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
					}
				}
			}
		}
	}

	fun rateAnswer(answerId: Int, rating: Int) {
		viewModelScope.launch {
			try {
				updateAnswerRating(answerId, AnswerStates.RATED, rating)
				navigateBack()
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(AnswerAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(AnswerAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(AnswerAction.ShowError(e.message))
					}

					else                            -> _actions.send(AnswerAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	fun returnAnswer(answerId: Int) {
		viewModelScope.launch {
			try {
				updateAnswerRating(answerId, AnswerStates.IN_PROGRESS)
				navigateBack()
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						e.response()?.errorBody()?.let { responseBody ->
							val errorMessage = responseBody.charStream().use { stream ->
								stream.readText()
							}
							_actions.send(AnswerAction.ShowError(errorMessage))
						} ?: run {
							_actions.send(AnswerAction.ShowError("Возникла непредвиденная ошибка"))
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(AnswerAction.ShowError(e.message))
					}

					else                            -> _actions.send(AnswerAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	private suspend fun checkAnswer(answerId: Int) {
		updateAnswerRating(answerId, AnswerStates.CHECKING)
	}

	fun showArtefact(artefactId: Int) {
		viewModelScope.launch {
			if (state.value is AnswerState.Content) {
				val content = state.value as AnswerState.Content
				try {
					val metaData = getArtefactMetaDataUseCase(artefactId = artefactId)
					val uri = getArtefactUseCase(metaData)
					_state.value = content.copy(
						file = uri,
					)
				} catch (e: Exception) {
					when (e) {
						is HttpException                -> {
							e.response()?.errorBody()?.let { responseBody ->
								val errorMessage = responseBody.charStream().use { stream ->
									stream.readText()
								}
								_actions.send(AnswerAction.ShowError(errorMessage))
							} ?: run {
								_actions.send(AnswerAction.ShowError("Возникла непредвиденная ошибка"))
							}
						}

						is NoNetworkConnectionException -> {
							_actions.send(AnswerAction.ShowError(e.message))
						}

						else                            -> _actions.send(AnswerAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
					}
				}
			}
		}
	}

	fun attachArtefact(fileName: String, file: ByteArray) {
		viewModelScope.launch {
			if (state.value is AnswerState.Content) {
				val content = state.value as AnswerState.Content
				try {
					_state.value = content.copy(
						metaData = postArtefactUseCase(fileName, file),
					)
				} catch (e: Exception) {
					when (e) {
						is HttpException                -> {
							e.response()?.errorBody()?.let { responseBody ->
								val errorMessage = responseBody.charStream().use { stream ->
									stream.readText()
								}
								_actions.send(AnswerAction.ShowError(errorMessage))
							} ?: run {
								_actions.send(AnswerAction.ShowError("Возникла непредвиденная ошибка"))
							}
						}

						is NoNetworkConnectionException -> {
							_actions.send(AnswerAction.ShowError(e.message))
						}

						else                            -> _actions.send(AnswerAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
					}
				}
			}
		}
	}

	fun detachArtefact() {
		viewModelScope.launch {
			if (state.value is AnswerState.Content) {
				val content = state.value as AnswerState.Content
				_state.value = content.copy(
					metaData = null,
				)
			}
		}
	}

	fun clearBuff() {
		if (state.value is AnswerState.Content) {
			_state.value = (state.value as AnswerState.Content).copy(
				file = null,
			)
		}
	}

	fun navigateBack() {
		router.goBack()
	}
}