package com.bobrovskii.forexam.navigation

import androidx.navigation.NavController
import com.bobrovskii.forexam.R
import com.bobrovskii.home.presentation.HomeNavigation
import com.bobrovskii.signin.presentation.SignInNavigation
import com.bobrovskii.signup.presentation.SignUpNavigation

class Navigator : SignInNavigation, SignUpNavigation, HomeNavigation {

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
}