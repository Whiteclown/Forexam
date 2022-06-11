package com.bobrovskii.home.presentation

interface HomeRouter {

	fun goBack()

	fun routeToEditExam(examId: Int)

	fun routeToAddExam()

	fun routeToAnswersList(examId: Int)

	fun routeToStudentsList(examId: Int)

	fun routeToSignIn()
}