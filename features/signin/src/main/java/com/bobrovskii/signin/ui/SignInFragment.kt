package com.bobrovskii.signin.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bobrovskii.signin.R
import com.bobrovskii.signin.databinding.FragmentSignInBinding
import com.bobrovskii.signin.presentation.SignInNavigation
import com.bobrovskii.signin.presentation.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

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
			viewModel.signIn(binding.editTextEmail.editText?.text.toString(), binding.editTextPassword.editText?.text.toString())
		}
		binding.textViewNoAcc.setOnClickListener {
			viewModel.navigateToSignUp()
		}
	}

	private fun initObservers() {
		lifecycleScope.launch {
			viewModel.isLogin.collect {
				val message = when (it) {
					401 -> "Пользователь с такими данными не найден!"
					else -> "$it has been received from server"
				}

				if (it != 0) {
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
					viewModel.changeErrorState()
				}
			}
		}
	}
}