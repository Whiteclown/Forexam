package com.bobrovskii.home.presentation.navigation

interface HomeNavigation {

	fun goBack()

	fun openEditExamination(examId: Int)

	fun routeToAddExam()

	fun routeToProgressExam(examId: Int)
}