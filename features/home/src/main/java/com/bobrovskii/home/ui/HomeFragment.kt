package com.bobrovskii.home.ui

import android.os.Bundle
import android.util.Log
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
import com.bobrovskii.home.presentation.periodsAdapter.PeriodsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), DeleteDialogFragment.DeleteDialogListener, StartExamDialogFragment.StartExamDialogListener {

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
		Log.d("debug", "start!")
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}

	private fun initRecyclerView() {
		binding.periodsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

		val adapterEditPeriods = PeriodsAdapter(
			onItemClicked = { examId ->
				viewModel.openEditExam(examId)
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.periodsEdit.collect {
				adapterEditPeriods.submitList(it)
				adapterEditPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterAllowancePeriods = PeriodsAdapter(
			onItemClicked = { examId ->
				viewModel.openAccessExam(examId)
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.periodsAllowance.collect {
				adapterAllowancePeriods.submitList(it)
				adapterAllowancePeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterReadyPeriods = PeriodsAdapter(
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
			viewModel.periodsReady.collect {
				adapterReadyPeriods.submitList(it)
				adapterReadyPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterProgressPeriods = PeriodsAdapter(
			onItemClicked = { examId ->
				//viewModel.openEditExam(examId)
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.periodsProgress.collect {
				adapterProgressPeriods.submitList(it)
				adapterProgressPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterFinishedPeriods = PeriodsAdapter(
			onItemClicked = { examId ->
				//viewModel.openEditExam(examId)
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.periodsFinished.collect {
				adapterFinishedPeriods.submitList(it)
				adapterFinishedPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterClosedPeriods = PeriodsAdapter(
			onItemClicked = { examId ->
				//viewModel.openEditExam(examId)
			},
			onDeleteClicked = { examId ->
				viewModel.setExamId(examId)
				showDeleteDialog()
			},
		)
		viewModel.viewModelScope.launch {
			viewModel.periodsClosed.collect {
				adapterClosedPeriods.submitList(it)
				adapterClosedPeriods.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		binding.periodsRV.adapter = ConcatAdapter(
			adapterProgressPeriods,
			adapterFinishedPeriods,
			adapterReadyPeriods,
			adapterAllowancePeriods,
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

	private fun showStartExamDialog() {
		val dialog = StartExamDialogFragment()
		dialog.show(childFragmentManager, "StartExamDialogFragment")
	}

	override fun onDialogNegativeClick(dialog: DialogFragment) {
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