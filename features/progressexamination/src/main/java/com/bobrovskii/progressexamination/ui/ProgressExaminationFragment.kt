package com.bobrovskii.progressexamination.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
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

	private var noAnswerAnswersAdapter: AnswersAdapter? = null
	private var inProgressAnswersAdapter: AnswersAdapter? = null
	private var sentAnswersAdapter: AnswersAdapter? = null
	private var checkingAnswersAdapter: AnswersAdapter? = null
	private var ratedAnswersAdapter: AnswersAdapter? = null
	private var noRatingAnswersAdapter: AnswersAdapter? = null

	private val examId: Int by lazy {
		arguments?.getInt(EXAM_ID) ?: throw IllegalStateException("no exam id")
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
	}

	private fun initRV() {
		binding.rvAnswers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		noAnswerAnswersAdapter = AnswersAdapter(onItemClicked = { viewModel.navigateToAnswer(it) })
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
			noAnswerAnswersAdapter, //todo: убрать
		)
	}

	private fun render(state: ProgressExaminationState) {
		when (state) {
			is ProgressExaminationState.Initial -> {}

			is ProgressExaminationState.Loading -> {}

			is ProgressExaminationState.Content -> {
				renderContentState(state)
			}
		}
	}

	private fun renderContentState(content: ProgressExaminationState.Content) {
		checkingAnswersAdapter?.answers = content.checkingAnswers
		sentAnswersAdapter?.answers = content.sentAnswers
		noRatingAnswersAdapter?.answers = content.noRatingAnswers
		inProgressAnswersAdapter?.answers = content.inProgressAnswers
		ratedAnswersAdapter?.answers = content.ratedAnswers
		noAnswerAnswersAdapter?.answers = content.noAnswerAnswers
	}
}