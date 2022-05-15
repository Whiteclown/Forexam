package com.bobrovskii.accessexamination.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobrovskii.accessexamination.R
import com.bobrovskii.accessexamination.databinding.FragmentAccessExamBinding
import com.bobrovskii.accessexamination.presentation.AccessExamState
import com.bobrovskii.accessexamination.presentation.AccessExamViewModel
import com.bobrovskii.accessexamination.ui.adapter.TicketsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccessExamFragment : Fragment(R.layout.fragment_access_exam) {

	private var _binding: FragmentAccessExamBinding? = null
	private val binding get() = _binding!!

	private val viewModel: AccessExamViewModel by viewModels()

	private val examId: Int by lazy {
		arguments?.getInt(EXAM_ID) ?: throw IllegalStateException("no examId")
	}

	private val adapter = TicketsAdapter(
		onSemesterRatingChanged = {ticketId, semesterRating ->
			viewModel.saveSemesterRating(ticketId, semesterRating)
		},
		onAllowanceChanged = {ticketId, allowance ->
			viewModel.saveAllowance(ticketId, allowance)
		},
	)

	companion object {

		private const val EXAM_ID = "EXAM_ID"

		fun createBundle(examId: Int) =
			Bundle().apply {
				putInt(EXAM_ID, examId)
			}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentAccessExamBinding.bind(view)
		initRV()
		initListeners()
		viewModel.viewModelScope.launch {
			viewModel.state.collect {
				if (it is AccessExamState.Content) {
					adapter.submitList(it.tickets)
					adapter.notifyDataSetChanged()
				}
			}
		}
		viewModel.loadData(examId)
	}

	private fun initListeners() {
		with(binding) {
			buttonSave.setOnClickListener {
				viewModel.saveData(false)
			}
			buttonSaveAndChangeState.setOnClickListener {
				viewModel.saveData(true)
			}
		}
	}

	private fun initRV() {
		binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		binding.recyclerView.adapter = adapter
		adapter.submitList(emptyList())
	}
}