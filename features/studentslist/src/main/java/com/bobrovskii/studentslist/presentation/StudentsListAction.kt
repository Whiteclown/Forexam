package com.bobrovskii.studentslist.presentation

sealed interface StudentsListAction {
	data class ShowError(val message: String) : StudentsListAction
}