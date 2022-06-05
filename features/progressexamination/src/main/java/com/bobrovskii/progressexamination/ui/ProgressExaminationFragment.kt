package com.bobrovskii.progressexamination.ui

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobrovskii.core.ExamStates
import com.bobrovskii.core.NOTIFICATION_ANSWER_FILTER
import com.bobrovskii.progressexamination.R
import com.bobrovskii.progressexamination.databinding.FragmentExaminationProgressBinding
import com.bobrovskii.progressexamination.presentation.ProgressExaminationState
import com.bobrovskii.progressexamination.presentation.ProgressExaminationViewModel
import com.bobrovskii.progressexamination.ui.answersAdapter.AnswersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProgressExaminationFragment : Fragment(R.layout.fragment_examination_progress) {

	private var _binding: FragmentExaminationProgressBinding? = null
	private val binding get() = _binding!!

	private val viewModel: ProgressExaminationViewModel by viewModels()

	private var inProgressAnswersAdapter: AnswersAdapter? = null
	private var sentAnswersAdapter: AnswersAdapter? = null
	private var checkingAnswersAdapter: AnswersAdapter? = null
	private var ratedAnswersAdapter: AnswersAdapter? = null
	private var noRatingAnswersAdapter: AnswersAdapter? = null

	private val examId: Int by lazy {
		arguments?.getInt(EXAM_ID) ?: throw IllegalStateException("no exam id")
	}

	private val notificationReceiver = object : BroadcastReceiver() {

		override fun onReceive(context: Context?, intent: Intent?) {
			Log.d("myTag", intent.toString())
			viewModel.refresh(examId)
		}
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
		initListeners()
		initRV()
		viewModel.state.onEach(::render).launchIn(viewModel.viewModelScope)
		viewModel.getAnswers(examId)
	}

	private fun initListeners() {
		binding.imageButtonBack.setOnClickListener { viewModel.navigateBack() }
		binding.btnEndExam.setOnClickListener {
			context?.let {
				AlertDialog
					.Builder(it)
					.setTitle("Внимание!")
					.setMessage("Вы уверены, что хотите ${binding.btnEndExam.text}?")
					.setNeutralButton("Нет") { _, _ -> }
					.setPositiveButton("Да") { _, _ ->
						viewModel.finishExam(examId)
					}
					.show()
			}
		}
	}

	private fun initRV() {
		binding.rvAnswers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		inProgressAnswersAdapter = AnswersAdapter(onItemClicked = { viewModel.navigateToAnswer(it) })
		sentAnswersAdapter = AnswersAdapter(onItemClicked = { viewModel.navigateToAnswer(it) })
		checkingAnswersAdapter = AnswersAdapter(onItemClicked = { viewModel.navigateToAnswer(it) })
		ratedAnswersAdapter = AnswersAdapter(onItemClicked = { viewModel.navigateToAnswer(it) })
		noRatingAnswersAdapter = AnswersAdapter(onItemClicked = { viewModel.navigateToAnswer(it) })
		binding.rvAnswers.adapter = ConcatAdapter(
			checkingAnswersAdapter,
			sentAnswersAdapter,
			noRatingAnswersAdapter,
			inProgressAnswersAdapter,
			ratedAnswersAdapter,
		)
	}

	private fun render(state: ProgressExaminationState) {
		if (state is ProgressExaminationState.Content) {
			if (state.examState == ExamStates.FINISHED) binding.btnEndExam.text = "закрыть экзамен"
			checkingAnswersAdapter?.answers = state.checkingAnswers
			sentAnswersAdapter?.answers = state.sentAnswers
			noRatingAnswersAdapter?.answers = state.noRatingAnswers
			inProgressAnswersAdapter?.answers = state.inProgressAnswers
			ratedAnswersAdapter?.answers = state.ratedAnswers
		}
		binding.loadingView.root.visibility = if (state is ProgressExaminationState.Loading) View.VISIBLE else View.GONE
	}

	override fun onResume() {
		super.onResume()
		requireActivity().registerReceiver(notificationReceiver, NOTIFICATION_ANSWER_FILTER)
	}

	override fun onPause() {
		super.onPause()
		requireActivity().unregisterReceiver(notificationReceiver)
	}
}