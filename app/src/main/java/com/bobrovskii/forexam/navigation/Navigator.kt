package com.bobrovskii.forexam.navigation

import androidx.navigation.NavController
import com.bobrovskii.forexam.R
import com.bobrovskii.home.presentation.navigation.AddExamNavigation
import com.bobrovskii.home.presentation.navigation.HomeNavigation
import com.bobrovskii.signin.presentation.SignInNavigation
import com.bobrovskii.signup.presentation.SignUpNavigation

class Navigator : SignInNavigation, SignUpNavigation, HomeNavigation, AddExamNavigation {

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

	override fun openAddExam() {
		navController?.navigate(R.id.action_homeFragment_to_addExamDialogFragment)
	}

	fun setToSignIn() {
		navController?.navigate(R.id.signInFragment)
	}

	fun setToHome() {
		navController?.navigate(R.id.homeFragment)
	}
}