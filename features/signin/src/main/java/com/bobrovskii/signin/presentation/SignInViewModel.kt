package com.bobrovskii.signin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.NoNetworkConnectionException
import com.bobrovskii.exam.domain.usecase.SendFirebaseTokenUseCase
import com.bobrovskii.session.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
	private val loginUseCase: LoginUseCase,
	private val sendFirebaseTokenUseCase: SendFirebaseTokenUseCase,
	private val router: SignInRouter,
) : ViewModel() {

	private val _actions: Channel<SignInAction> = Channel(Channel.BUFFERED)
	val actions: Flow<SignInAction> = _actions.receiveAsFlow()

	fun signIn(email: String, password: String) {
		viewModelScope.launch {
			try {
				loginUseCase(username = email, password = password)
				sendFirebaseTokenUseCase()
				navigateToHome()
			} catch (e: Exception) {
				when (e) {
					is HttpException                -> {
						if (e.code() == 401) {
							_actions.send(SignInAction.ShowError("Пользователь с такими данными не найден!"))
						} else {
							e.response()?.errorBody()?.let { responseBody ->
								val errorMessage = responseBody.charStream().use { stream ->
									stream.readText()
								}
								_actions.send(SignInAction.ShowError(errorMessage))
							} ?: run {
								_actions.send(SignInAction.ShowError("Возникла непредвиденная ошибка"))
							}
						}
					}

					is NoNetworkConnectionException -> {
						_actions.send(SignInAction.ShowError(e.message))
					}

					else                            -> _actions.send(SignInAction.ShowError(e.message ?: "Возникла непредвиденная ошибка"))
				}
			}
		}
	}

	private fun navigateToHome() {
		router.routeToHome()
	}

	fun navigateToSignUp() {
		router.routeToSignUp()
	}

}