package com.bobrovskii.progressexamination.presentation

sealed interface ProgressExaminationAction {
	data class ShowError(val message: String) : ProgressExaminationAction
}