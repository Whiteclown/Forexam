package com.bobrovskii.home.presentation

sealed interface HomeAction {
	data class ShowError(val message: String) : HomeAction
}