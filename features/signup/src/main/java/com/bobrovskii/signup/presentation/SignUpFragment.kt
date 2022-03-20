package com.bobrovskii.signup.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bobrovskii.signup.R
import com.bobrovskii.signup.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

	@Inject
	lateinit var navigation: SignUpNavigation

	private lateinit var binding: FragmentSignUpBinding

	private val viewModel: SignUpViewModel by viewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding = FragmentSignUpBinding.bind(view)
		initListeners()
	}

	private fun initListeners() {
		binding.buttonRegistration.setOnClickListener {
			navigation.goBack()
		}
	}
}