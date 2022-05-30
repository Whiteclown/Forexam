package com.bobrovskii.progressexamination.presentation

interface ProgressExaminationRouter {

	fun goBack()

	fun routeToAnswer(answerId: Int)
}