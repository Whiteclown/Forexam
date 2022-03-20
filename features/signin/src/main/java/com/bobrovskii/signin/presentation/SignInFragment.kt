package com.bobrovskii.signin.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bobrovskii.signin.R
import com.bobrovskii.signin.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

	@Inject
	lateinit var navigation: SignInNavigation

	private lateinit var binding: FragmentSignInBinding

	private val viewModel: SignInViewModel by viewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding = FragmentSignInBinding.bind(view)
		initListeners()
		initObservers()
	}

	private fun initListeners() {
		binding.buttonSignIn.setOnClickListener {
			viewModel.signIn(binding.editTextEmail.text.toString())
			navigation.openHome()
		}
		binding.textViewNoAcc.setOnClickListener {
			navigation.openSignUp()
		}
	}

	private fun initObservers() {
		lifecycleScope.launch {
			viewModel.token.collect {
				binding.screenName.text = it.toString()
			}
		}
	}
}