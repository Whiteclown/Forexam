package com.bobrovskii.signin.presentation

import androidx.lifecycle.ViewModel
import com.bobrovskii.signin.domain.entity.UserCredentials
import com.bobrovskii.signin.domain.usecase.GetTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
	private val getTokenUseCase: GetTokenUseCase
) : ViewModel() {

	private val _token = MutableStateFlow(false)

	val token: StateFlow<Boolean> = _token

	fun signIn(email: String) {
		_token.value = getTokenUseCase(UserCredentials(email = email))
	}

}