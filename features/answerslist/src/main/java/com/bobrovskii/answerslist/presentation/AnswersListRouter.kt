package com.bobrovskii.answerslist.presentation

interface AnswersListRouter {

	fun goBack()

	fun routeToAnswer(answerId: Int)
}