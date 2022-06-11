package com.bobrovskii.studentslist.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobrovskii.core.ExamStates
import com.bobrovskii.core.IOnBackPressed
import com.bobrovskii.studentslist.R
import com.bobrovskii.studentslist.databinding.FragmentStudentsListBinding
import com.bobrovskii.studentslist.presentation.StudentsListAction
import com.bobrovskii.studentslist.presentation.StudentsListState
import com.bobrovskii.studentslist.presentation.StudentsListViewModel
import com.bobrovskii.studentslist.ui.answersAdapter.AnswersAdapter
import com.bobrovskii.studentslist.ui.studentsAdapter.StudentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class StudentsListFragment : Fragment(R.layout.fragment_students_list), IOnBackPressed {

	private var _binding: FragmentStudentsListBinding? = null
	private val binding get() = _binding!!

	private val viewModel: StudentsListViewModel by viewModels()

	private var isGoingBack = true

	private var studentsAdapter: StudentsAdapter? = null

	private var questionsAdapter: AnswersAdapter? = null
	private var exercisesAdapter: AnswersAdapter? = null

	private val examId: Int by lazy {
		arguments?.getInt(EXAM_ID) ?: throw IllegalStateException("no exam id")
	}

//	private val notificationReceiver = object : BroadcastReceiver() {
//
//		override fun onReceive(context: Context?, intent: Intent?) {
//			Log.d("myTag", intent.toString())
//			viewModel.refresh(examId)
//		}
//	}

	companion object {

		private const val EXAM_ID = "EXAM_ID"

		fun createBundle(examId: Int) =
			Bundle().apply {
				putInt(EXAM_ID, examId)
			}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentStudentsListBinding.bind(view)
		initListeners()
		initRV()
		lifecycleScope.launch {
			viewModel.actions.collect { handleAction(it) }
		}
		viewModel.state.onEach(::render).launchIn(viewModel.viewModelScope)
	}

	private fun initListeners() {
		binding.imageButtonBack.setOnClickListener {
			if (isGoingBack) {
				viewModel.navigateBack()
			} else {
				viewModel.turnFilterOff()
			}
		}
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
		questionsAdapter = AnswersAdapter { viewModel.navigateToAnswer(it) }
		exercisesAdapter = AnswersAdapter { viewModel.navigateToAnswer(it) }
		binding.rvAnswers.adapter = ConcatAdapter(questionsAdapter, exercisesAdapter)

		binding.rvStudents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		studentsAdapter = StudentsAdapter { viewModel.filterByName(it) }
		binding.rvStudents.adapter = studentsAdapter
	}

	private fun render(state: StudentsListState) {
		if (state is StudentsListState.Content) {
			if (state.examState == ExamStates.FINISHED) binding.btnEndExam.text = "закрыть экзамен"
			state.filterStudentName?.let { filterStudentName ->
				binding.rvStudents.visibility = View.GONE
				binding.rvAnswers.visibility = View.VISIBLE
				questionsAdapter?.answers =
					state.questionAnswers.filter { it.studentName?.lowercase(Locale.ROOT)?.contains(filterStudentName.lowercase(Locale.ROOT)) ?: true }
				exercisesAdapter?.answers =
					state.exerciseAnswers.filter { it.studentName?.lowercase(Locale.ROOT)?.contains(filterStudentName.lowercase(Locale.ROOT)) ?: true }
				isGoingBack = false
			} ?: run {
				binding.rvAnswers.visibility = View.GONE
				binding.rvStudents.visibility = View.VISIBLE
				studentsAdapter?.students = state.students
				isGoingBack = true
			}
			with(binding) {
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
		binding.btnEndExam.visibility = if (state is StudentsListState.Content && state.examState != ExamStates.CLOSED) View.VISIBLE else View.GONE
		binding.loadingView.root.visibility = if (state is StudentsListState.Loading) View.VISIBLE else View.GONE
	}

	override fun onResume() {
		super.onResume()
//		requireActivity().registerReceiver(notificationReceiver, NOTIFICATION_ANSWER_FILTER)
		viewModel.getAnswers(examId)
	}

	override fun onPause() {
		super.onPause()
//		requireActivity().unregisterReceiver(notificationReceiver)
	}

	private fun handleAction(action: StudentsListAction) {
		when (action) {
			is StudentsListAction.ShowError -> {
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

	override fun onBackPressed() =
		if (isGoingBack) {
			isGoingBack
		} else {
			isGoingBack.let {
				viewModel.turnFilterOff()
				it
			}
		}
}