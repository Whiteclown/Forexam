package com.bobrovskii.editexamination.presentation

sealed interface EditExaminationAction {
	data class ShowError(val message: String) : EditExaminationAction
}