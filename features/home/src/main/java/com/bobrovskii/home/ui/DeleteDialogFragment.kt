package com.bobrovskii.home.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.bobrovskii.home.R
import com.bobrovskii.home.databinding.FragmentDialogDeleteBinding

class DeleteDialogFragment : DialogFragment(R.layout.fragment_dialog_delete) {

	private var _binding: FragmentDialogDeleteBinding? = null
	private val binding get() = _binding!!

	internal lateinit var listener: DeleteDialogListener

	interface DeleteDialogListener {

		fun onDialogNegativeClick(dialog: DialogFragment)
		fun onDialogDeleteClick(dialog: DialogFragment)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentDialogDeleteBinding.bind(view)
		binding.btnDelete.setOnClickListener { listener.onDialogDeleteClick(this) }
		binding.btnCancel.setOnClickListener { listener.onDialogNegativeClick(this) }
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		try {
			listener = parentFragment as DeleteDialogListener
		} catch (e: ClassCastException) {
			throw ClassCastException("$context must implement DeleteDialogInterface")
		}
	}
}