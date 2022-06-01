package com.bobrovskii.forexam.navigation

import androidx.navigation.NavController
import com.bobrovskii.addexamination.presentation.AddExamRouter
import com.bobrovskii.editexamination.presentation.EditExaminationRouter
import com.bobrovskii.editexamination.ui.EditExaminationFragment
import com.bobrovskii.forexam.R
import com.bobrovskii.home.presentation.HomeRouter
import com.bobrovskii.progressexamination.presentation.ProgressExaminationRouter
import com.bobrovskii.progressexamination.ui.ProgressExaminationFragment
import com.bobrovskii.signin.presentation.SignInRouter
import com.bobrovskii.signup.presentation.SignUpRouter
import presentation.AnswerRouter
import ui.AnswerFragment

class Navigator :
	SignInRouter,
	SignUpRouter,
	HomeRouter,
	EditExaminationRouter,
	AddExamRouter,
	ProgressExaminationRouter,
	AnswerRouter {

	private var navController: NavController? = null

	fun bind(navController: NavController) {
		this.navController = navController
	}

	fun unbind() {
		navController = null
	}

	override fun routeToSignUp() {
		navController?.navigate(R.id.action_signInFragment_to_signUpFragment)
	}

	override fun routeToHome() {
		navController?.navigate(R.id.action_signInFragment_to_homeFragment)
	}

	override fun goBack() {
		navController?.popBackStack()
	}

	override fun routeToAnswer(answerId: Int) {
		navController?.navigate(
			R.id.action_progressExaminationFragment_to_answerFragment,
			AnswerFragment.createBundle(answerId)
		)
	}

	override fun routeToEditExam(examId: Int) {
		navController?.navigate(
			R.id.action_homeFragment_to_editExaminationFragment,
			EditExaminationFragment.createBundle(examId)
		)
	}

	override fun routeToAddExam() {
		navController?.navigate(R.id.action_homeFragment_to_addExamFragment)
	}

	override fun routeToProgressExam(examId: Int) {
		navController?.navigate(
			R.id.action_homeFragment_to_progressExaminationFragment,
			ProgressExaminationFragment.createBundle(examId)
		)
	}

	fun setToSignIn() {
		navController?.navigate(R.id.signInFragment)
	}

	fun setToHome() {
		navController?.navigate(R.id.homeFragment)
	}

	override fun routeBack() {
		navController?.popBackStack()
	}
}