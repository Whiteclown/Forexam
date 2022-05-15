package com.bobrovskii.forexam.navigation

import androidx.navigation.NavController
import com.bobrovskii.accessexamination.presentation.AccessExamNavigation
import com.bobrovskii.accessexamination.ui.AccessExamFragment
import com.bobrovskii.addexamination.presentation.AddExamRouter
import com.bobrovskii.editexamination.presentation.EditExaminationNavigation
import com.bobrovskii.editexamination.ui.EditExaminationFragment
import com.bobrovskii.forexam.R
import com.bobrovskii.home.presentation.navigation.HomeNavigation
import com.bobrovskii.signin.presentation.SignInNavigation
import com.bobrovskii.signup.presentation.SignUpNavigation

class Navigator : SignInNavigation, SignUpNavigation, HomeNavigation, EditExaminationNavigation, AddExamRouter, AccessExamNavigation {

	private var navController: NavController? = null

	fun bind(navController: NavController) {
		this.navController = navController
	}

	fun unbind() {
		navController = null
	}

	override fun openSignUp() {
		navController?.navigate(R.id.action_signInFragment_to_signUpFragment)
	}

	override fun openHome() {
		navController?.navigate(R.id.action_signInFragment_to_homeFragment)
	}

	override fun goBack() {
		navController?.popBackStack()
	}

	override fun openEditExamination(examId: Int) {
		navController?.navigate(
			R.id.action_homeFragment_to_editExaminationFragment,
			EditExaminationFragment.createBundle(examId)
		)
	}

	override fun routeToAccessExam(examId: Int) {
		navController?.navigate(
			R.id.action_homeFragment_to_accessExamFragment,
			AccessExamFragment.createBundle(examId)
		)
	}

	override fun routeToAddExam() {
		navController?.navigate(R.id.action_homeFragment_to_addExamFragment)
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