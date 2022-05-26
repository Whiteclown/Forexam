package com.bobrovskii.progressexamination.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bobrovskii.progressexamination.R
import com.bobrovskii.progressexamination.databinding.FragmentExaminationProgressBinding
import com.bobrovskii.progressexamination.presentation.ProgressExaminationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgressExaminationFragment : Fragment(R.layout.fragment_examination_progress) {

	private var _binding: FragmentExaminationProgressBinding? = null
	val binding = _binding!!

	private val viewModel: ProgressExaminationViewModel by viewModels()

	private val examId: Int by lazy {
		arguments?.getInt(EXAM_ID) ?: throw IllegalStateException("no examId")
	}

	companion object {

		private const val EXAM_ID = "EXAM_ID"

		fun createBundle(examId: Int) =
			Bundle().apply {
				putInt(EXAM_ID, examId)
			}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentExaminationProgressBinding.bind(view)
	}
}