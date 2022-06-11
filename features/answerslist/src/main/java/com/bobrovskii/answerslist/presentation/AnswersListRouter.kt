package com.bobrovskii.answerslist.presentation

interface AnswersListRouter {

	fun goBack()

	fun routeFromAnswersListToAnswer(answerId: Int, isClosed: Boolean)
}