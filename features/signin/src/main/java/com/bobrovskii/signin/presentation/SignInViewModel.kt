package com.bobrovskii.signin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.exam.domain.usecase.SendFirebaseTokenUseCase
import com.bobrovskii.session.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
	private val loginUseCase: LoginUseCase,
	private val sendFirebaseTokenUseCase: SendFirebaseTokenUseCase,
	private val router: SignInRouter,
) : ViewModel() {

	private val _isError = MutableStateFlow(0)
	val isLogin: StateFlow<Int> = _isError

	fun signIn(email: String, password: String) {
		viewModelScope.launch {
			try {
				loginUseCase(username = email, password = password)
				sendFirebaseTokenUseCase()
				navigateToHome()
			} catch (e: HttpException) {
				_isError.value = e.code()
			}
		}
	}

	fun changeErrorState() {
		_isError.value = 0
	}

	private fun navigateToHome() {
		router.routeToHome()
	}

	fun navigateToSignUp() {
		router.routeToSignUp()
	}

}