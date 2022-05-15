package com.bobrovskii.home.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.bobrovskii.home.R
import com.bobrovskii.home.databinding.FragmentDialogStartExamBinding

class StartExamDialogFragment : DialogFragment(R.layout.fragment_dialog_start_exam) {

	private var _binding: FragmentDialogStartExamBinding? = null
	private val binding get() = _binding!!

	internal lateinit var listener: StartExamDialogListener

	interface StartExamDialogListener {

		fun onDialogNegativeClick(dialog: DialogFragment)
		fun onDialogStartClick(dialog: DialogFragment)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentDialogStartExamBinding.bind(view)
		binding.btnStart.setOnClickListener { listener.onDialogStartClick(this) }
		binding.btnCancel.setOnClickListener { listener.onDialogNegativeClick(this) }
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		try {
			listener = parentFragment as StartExamDialogListener
		} catch (e: ClassCastException) {
			throw ClassCastException("$context must implement DeleteDialogInterface")
		}
	}
}