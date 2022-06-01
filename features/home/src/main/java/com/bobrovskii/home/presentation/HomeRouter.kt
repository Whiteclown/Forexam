package com.bobrovskii.home.presentation

interface HomeRouter {

	fun goBack()

	fun routeToEditExam(examId: Int)

	fun routeToAddExam()

	fun routeToProgressExam(examId: Int)
}