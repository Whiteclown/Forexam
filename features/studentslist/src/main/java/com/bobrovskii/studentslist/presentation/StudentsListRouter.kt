package com.bobrovskii.studentslist.presentation

interface StudentsListRouter {

	fun goBack()

	fun routeFromStudentsListToAnswer(answerId: Int, isClosed: Boolean)
}