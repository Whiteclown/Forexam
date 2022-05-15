package com.bobrovskii.home.presentation.navigation

interface HomeNavigation {

	fun goBack()

	fun openEditExamination(examId: Int)

	fun routeToAccessExam(examId: Int)

	fun routeToAddExam()
}