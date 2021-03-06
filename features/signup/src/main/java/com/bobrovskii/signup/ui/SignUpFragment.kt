package com.bobrovskii.signup.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bobrovskii.signup.R
import com.bobrovskii.signup.databinding.FragmentSignUpBinding
import com.bobrovskii.signup.presentation.SignUpRouter
import com.bobrovskii.signup.presentation.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

	@Inject
	lateinit var navigation: SignUpRouter

	private lateinit var binding: FragmentSignUpBinding

	private val viewModel: SignUpViewModel by viewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding = FragmentSignUpBinding.bind(view)
		initListeners()
	}

	private fun initListeners() {
		binding.imageButtonBack.setOnClickListener {
			navigation.goBack()
		}
		binding.buttonRegistration.setOnClickListener {
			viewModel.signUp()
		}
	}
}