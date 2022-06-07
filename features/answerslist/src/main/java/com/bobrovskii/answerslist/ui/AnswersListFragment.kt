package com.bobrovskii.answerslist.ui

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobrovskii.answerslist.R
import com.bobrovskii.answerslist.databinding.FragmentAnswersListBinding
import com.bobrovskii.answerslist.presentation.AnswersListAction
import com.bobrovskii.answerslist.presentation.AnswersListState
import com.bobrovskii.answerslist.presentation.AnswersListViewModel
import com.bobrovskii.answerslist.ui.answersAdapter.AnswersAdapter
import com.bobrovskii.core.ExamStates
import com.bobrovskii.core.NOTIFICATION_ANSWER_FILTER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class AnswersListFragment : Fragment(R.layout.fragment_answers_list) {

	private var _binding: FragmentAnswersListBinding? = null
	private val binding get() = _binding!!

	private val viewModel: AnswersListViewModel by viewModels()

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
		_binding = FragmentAnswersListBinding.bind(view)
		initListeners()
		initRV()
		lifecycleScope.launch {
			viewModel.actions.collect { handleAction(it) }
		}
		viewModel.state.onEach(::render).launchIn(viewModel.viewModelScope)
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
		binding.etStudentFilterName.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

			override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
				viewModel.filterByName(p0.toString())
			}

			override fun afterTextChanged(p0: Editable?) = Unit
		})
		binding.btnTurnOffFilter.setOnClickListener { binding.etStudentFilterName.setText("") }
	}

	private fun initRV() {
		binding.rvAnswers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		inProgressAnswersAdapter = AnswersAdapter(
			onItemClicked = { viewModel.navigateToAnswer(it) },
		)
		sentAnswersAdapter = AnswersAdapter(
			onItemClicked = { viewModel.navigateToAnswer(it) },
		)
		checkingAnswersAdapter = AnswersAdapter(
			onItemClicked = { viewModel.navigateToAnswer(it) },
		)
		ratedAnswersAdapter = AnswersAdapter(
			onItemClicked = { viewModel.navigateToAnswer(it) },
		)
		noRatingAnswersAdapter = AnswersAdapter(
			onItemClicked = { viewModel.navigateToAnswer(it) },
		)
		binding.rvAnswers.adapter = ConcatAdapter(
			checkingAnswersAdapter,
			sentAnswersAdapter,
			noRatingAnswersAdapter,
			inProgressAnswersAdapter,
			ratedAnswersAdapter,
		)
	}

	private fun render(state: AnswersListState) {
		if (state is AnswersListState.Content) {
			if (state.examState == ExamStates.FINISHED) binding.btnEndExam.text = "закрыть экзамен"
			binding.btnTurnOffFilter.visibility = if (binding.etStudentFilterName.text.toString() == "") View.GONE else View.VISIBLE
			state.filterStudentName?.let { filterStudentName ->
				checkingAnswersAdapter?.answers =
					state.checkingAnswers.filter { it.studentName?.lowercase(Locale.ROOT)?.contains(filterStudentName.lowercase(Locale.ROOT)) ?: true }
				sentAnswersAdapter?.answers =
					state.sentAnswers.filter { it.studentName?.lowercase(Locale.ROOT)?.contains(filterStudentName.lowercase(Locale.ROOT)) ?: true }
				noRatingAnswersAdapter?.answers =
					state.noRatingAnswers.filter { it.studentName?.lowercase(Locale.ROOT)?.contains(filterStudentName.lowercase(Locale.ROOT)) ?: true }
				inProgressAnswersAdapter?.answers =
					state.inProgressAnswers.filter { it.studentName?.lowercase(Locale.ROOT)?.contains(filterStudentName.lowercase(Locale.ROOT)) ?: true }
				ratedAnswersAdapter?.answers =
					state.ratedAnswers.filter { it.studentName?.lowercase(Locale.ROOT)?.contains(filterStudentName.lowercase(Locale.ROOT)) ?: true }
			} ?: run {
				checkingAnswersAdapter?.answers = state.checkingAnswers
				sentAnswersAdapter?.answers = state.sentAnswers
				noRatingAnswersAdapter?.answers = state.noRatingAnswers
				inProgressAnswersAdapter?.answers = state.inProgressAnswers
				ratedAnswersAdapter?.answers = state.ratedAnswers
			}
			with(binding) {
				tvCounterChecking.text = state.checkingCounter.toString()
				tvCounterInProgress.text = state.inProgressCounter.toString()
				tvCounterSent.text = state.sentCounter.toString()
				if (state.examState == ExamStates.CLOSED) {
					ivCounterSent.visibility = View.GONE
					ivCounterChecking.visibility = View.GONE
					ivCounterInProgress.visibility = View.GONE
					tvCounterChecking.visibility = View.GONE
					tvCounterInProgress.visibility = View.GONE
					tvCounterSent.visibility = View.GONE
				}
			}
		}
		binding.btnEndExam.visibility = if (state is AnswersListState.Content && state.examState != ExamStates.CLOSED) View.VISIBLE else View.GONE
		binding.loadingView.root.visibility = if (state is AnswersListState.Loading) View.VISIBLE else View.GONE
	}

	override fun onResume() {
		super.onResume()
		requireActivity().registerReceiver(notificationReceiver, NOTIFICATION_ANSWER_FILTER)
		viewModel.getAnswers(examId)
	}

	override fun onPause() {
		super.onPause()
		requireActivity().unregisterReceiver(notificationReceiver)
	}

	private fun handleAction(action: AnswersListAction) {
		when (action) {
			is AnswersListAction.ShowError -> {
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