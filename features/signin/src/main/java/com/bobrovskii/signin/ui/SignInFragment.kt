package com.bobrovskii.signin.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bobrovskii.core.IOnBackPressed
import com.bobrovskii.signin.R
import com.bobrovskii.signin.databinding.FragmentSignInBinding
import com.bobrovskii.signin.presentation.SignInAction
import com.bobrovskii.signin.presentation.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in), IOnBackPressed {

	private var _binding: FragmentSignInBinding? = null
	private val binding get() = _binding!!

	private val viewModel: SignInViewModel by viewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentSignInBinding.bind(view)
		lifecycleScope.launch {
			viewModel.actions.collect { handleAction(it) }
		}
		initListeners()
	}

	private fun initListeners() {
		binding.buttonSignIn.setOnClickListener {
			viewModel.signIn(binding.editTextEmail.editText?.text.toString(), binding.editTextPassword.editText?.text.toString())
		}
		binding.textViewNoAcc.setOnClickListener {
			viewModel.navigateToSignUp()
		}
	}

	private fun handleAction(action: SignInAction) {
		when (action) {
			is SignInAction.ShowError -> {
				val message = SpannableString(action.message)
				message.setSpan(
					ForegroundColorSpan(Color.BLACK),
					0,
					message.length,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
				)
				context?.let {
					AlertDialog
						.Builder(it)
						.setTitle("Ошибка")
						.setMessage(message)
						.setNeutralButton("Ок") { _, _ -> }
						.show()
				}
			}
		}
	}

	override fun onBackPressed() = true
}