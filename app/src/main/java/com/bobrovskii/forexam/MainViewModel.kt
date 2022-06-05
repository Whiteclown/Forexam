package com.bobrovskii.forexam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobrovskii.exam.domain.usecase.SendFirebaseTokenUseCase
import com.bobrovskii.forexam.navigation.Navigator
import com.bobrovskii.session.domain.usecase.GetSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val getSessionUseCase: GetSessionUseCase,
	private val sendFirebaseTokenUseCase: SendFirebaseTokenUseCase,
) : ViewModel() {

	@Inject
	lateinit var navigation: Navigator

	init {
		viewModelScope.launch(Dispatchers.Main) {
			runCatching {
				getSessionUseCase()
			}.onSuccess {
				sendFirebaseTokenUseCase()
				navigation.routeToHome()
			}.onFailure {
				navigation.setToSignIn()
			}
		}
	}
}