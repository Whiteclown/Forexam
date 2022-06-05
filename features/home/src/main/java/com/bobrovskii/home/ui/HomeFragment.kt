package com.bobrovskii.home.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobrovskii.home.databinding.FragmentHomeBinding
import com.bobrovskii.home.presentation.HomeAction
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
		lifecycleScope.launch {
			viewModel.actions.collect { handleAction(it) }
		}
		initRecyclerView()
		initListeners()
	}

	override fun onStart() {
		super.onStart()
		viewModel.getExams()
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}

	private fun initRecyclerView() {
		binding.rvExams.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

		val adapterEditExams = ExamsAdapter(
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
				adapterEditExams.submitList(it)
				adapterEditExams.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterReadyExams = ExamsAdapter(
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
				adapterReadyExams.submitList(it)
				adapterReadyExams.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterTimesetExams = ExamsAdapter(
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
				adapterTimesetExams.submitList(it)
				adapterTimesetExams.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterProgressExams = ExamsAdapter(
			onItemClicked = { examId ->
				viewModel.openProgressExam(examId)
			},
			onDeleteClicked = { },
		)
		viewModel.viewModelScope.launch {
			viewModel.examsProgress.collect {
				adapterProgressExams.submitList(it)
				adapterProgressExams.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterFinishedExams = ExamsAdapter(
			onItemClicked = { examId ->
				viewModel.openProgressExam(examId)
			},
			onDeleteClicked = { },
		)
		viewModel.viewModelScope.launch {
			viewModel.examsFinished.collect {
				adapterFinishedExams.submitList(it)
				adapterFinishedExams.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		val adapterClosedExams = ExamsAdapter(
			onItemClicked = { examId ->

			},
			onDeleteClicked = { },
		)
		viewModel.viewModelScope.launch {
			viewModel.examsClosed.collect {
				adapterClosedExams.submitList(it)
				adapterClosedExams.notifyDataSetChanged()
				binding.swipe.isRefreshing = false
			}
		}

		binding.rvExams.adapter = ConcatAdapter(
			adapterProgressExams,
			adapterFinishedExams,
			adapterReadyExams,
			adapterTimesetExams,
			adapterEditExams,
			adapterClosedExams,
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
				viewModel.getExams()
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

	private fun handleAction(action: HomeAction) {
		when (action) {
			is HomeAction.ShowError -> {
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
}