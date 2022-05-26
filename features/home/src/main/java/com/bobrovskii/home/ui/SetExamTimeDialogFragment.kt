package com.bobrovskii.home.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.bobrovskii.core.ShowDateAndTimePicker
import com.bobrovskii.home.R
import com.bobrovskii.home.databinding.FragmentDialogSetExamTimeBinding

class SetExamTimeDialogFragment : DialogFragment(R.layout.fragment_dialog_set_exam_time) {

	private var _binding: FragmentDialogSetExamTimeBinding? = null
	private val binding get() = _binding!!

	internal lateinit var listener: SetExamTimeDialogListener

	interface SetExamTimeDialogListener {

		fun onDialogNegativeClick(dialog: DialogFragment)
		fun onDialogSetTimeClick(dialog: DialogFragment, date: String)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentDialogSetExamTimeBinding.bind(view)
		binding.btnStart.setOnClickListener { listener.onDialogSetTimeClick(this, binding.btnSetTime.text.toString()) }
		binding.btnCancel.setOnClickListener { listener.onDialogNegativeClick(this) }
		binding.btnSetTime.setOnClickListener { context?.let { it1 -> ShowDateAndTimePicker(it1, it) } }
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		try {
			listener = parentFragment as SetExamTimeDialogListener
		} catch (e: ClassCastException) {
			throw ClassCastException("$context must implement DeleteDialogInterface")
		}
	}
}