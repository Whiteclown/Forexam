package com.bobrovskii.addexamination.presentation

sealed interface AddExamAction {
	data class ShowError(val message: String) : AddExamAction
}