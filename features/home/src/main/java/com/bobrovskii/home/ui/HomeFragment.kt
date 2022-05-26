package com.bobrovskii.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobrovskii.home.databinding.FragmentHomeBinding
import com.bobrovskii.home.presentation.HomeViewModel
import com.bobrovskii.home.ui.examsAdapter.ExamsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(),
					 DeleteDialogFragment.DeleteDialogListener,
					 SetExamTimeDialogFragment.SetExamTimeDialogListener,
					 StartExamDialogFragment.StartExamDialogListener {

	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!

	private val viewModel: HomeViewModel by viewModels()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentHomeBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initRecyclerView()
		initListeners()
	}

	override fun onStart() {
		super.onStart()
		viewModel.getPeriods()
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}

	private fun initRecyclerView() {
		binding.periodsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

		val adapterEditPeriods = ExamsAdapter(
			onItemClicked = { examId ->
				viewModel.openEditExam(examId)
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.examsEdit.collect {
				adapterEditPeriods.submitList(it)
				adapterEditPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterReadyPeriods = ExamsAdapter(
			onItemClicked = { examId ->
				viewModel.setExamId(examId)
				showSetExamTimeDialog()
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.examsReady.collect {
				adapterReadyPeriods.submitList(it)
				adapterReadyPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterTimesetPeriods = ExamsAdapter(
			onItemClicked = { examId ->
				viewModel.setExamId(examId)
				showStartExamDialog()
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.examsTimeset.collect {
				adapterTimesetPeriods.submitList(it)
				adapterTimesetPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterProgressPeriods = ExamsAdapter(
			onItemClicked = { examId ->
				viewModel.openProgressExam(examId)
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.examsProgress.collect {
				adapterProgressPeriods.submitList(it)
				adapterProgressPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterFinishedPeriods = ExamsAdapter(
			onItemClicked = { examId ->
				//viewModel.openEditExam(examId)
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.examsFinished.collect {
				adapterFinishedPeriods.submitList(it)
				adapterFinishedPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterClosedPeriods = ExamsAdapter(
			onItemClicked = { examId ->
				//viewModel.openEditExam(examId)
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.examsClosed.collect {
				adapterClosedPeriods.submitList(it)
				adapterClosedPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		binding.periodsRV.adapter = ConcatAdapter(
			adapterProgressPeriods,
			adapterFinishedPeriods,
			adapterReadyPeriods,
			adapterTimesetPeriods,
			adapterEditPeriods,
			adapterClosedPeriods,
		)
	}

	private fun initListeners() {
		with(binding) {
			imageButtonExit.setOnClickListener {
				viewModel.goBack()
			}
			imageButtonAdd.setOnClickListener {
				viewModel.openAddExam()
			}
			swipe.setOnRefreshListener {
				viewModel.getPeriods()
			}
		}
	}

	private fun showDeleteDialog() {
		val dialog = DeleteDialogFragment()
		dialog.show(childFragmentManager, "DeleteDialogFragment")
	}

	private fun showSetExamTimeDialog() {
		val dialog = SetExamTimeDialogFragment()
		dialog.show(childFragmentManager, "SetExamTimeDialogFragment")
	}

	private fun showStartExamDialog() {
		val dialog = StartExamDialogFragment()
		dialog.show(childFragmentManager, "StartExamDialogFragment")
	}

	override fun onDialogNegativeClick(dialog: DialogFragment) {
		dialog.dismiss()
	}

	override fun onDialogSetTimeClick(dialog: DialogFragment, date: String) {
		viewModel.changeStateToTimeset(date)
		dialog.dismiss()
	}

	override fun onDialogStartClick(dialog: DialogFragment) {
		viewModel.changeStateToProgress()
		dialog.dismiss()
	}

	override fun onDialogDeleteClick(dialog: DialogFragment) {
		viewModel.deleteExam()
		dialog.dismiss()
	}
}