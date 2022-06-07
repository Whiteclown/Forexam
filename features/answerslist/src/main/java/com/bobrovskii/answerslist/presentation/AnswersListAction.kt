package com.bobrovskii.answerslist.presentation

sealed interface AnswersListAction {
	data class ShowError(val message: String) : AnswersListAction
}