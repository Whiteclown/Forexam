package com.bobrovskii.signin.presentation

sealed interface SignInAction {
	data class ShowError(val message: String) : SignInAction
}