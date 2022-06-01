package ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobrovskii.answer.R
import com.bobrovskii.answer.databinding.FragmentAnswerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import presentation.AnswerState
import presentation.AnswerViewModel
import ui.messageAdapter.MessageAdapter

@AndroidEntryPoint
class AnswerFragment : Fragment(R.layout.fragment_answer) {

	private var _binding: FragmentAnswerBinding? = null
	private val binding get() = _binding!!

	private val viewModel: AnswerViewModel by viewModels()

	private val answerId: Int by lazy {
		arguments?.getInt(ANSWER_ID) ?: throw IllegalStateException("no answer id")
	}

	private val messageAdapter = MessageAdapter()

	companion object {

		private const val ANSWER_ID = "ANSWER_ID"

		fun createBundle(answerId: Int) =
			Bundle().apply {
				putInt(ANSWER_ID, answerId)
			}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentAnswerBinding.bind(view)
		initRV()
		initListeners()
		viewModel.state.onEach(::render).launchIn(viewModel.viewModelScope)
		viewModel.loadData(answerId)
	}

	private fun initListeners() {
		with(binding) {
			btnSendMessage.setOnClickListener {
				viewModel.sendMessage(answerId, etMessageText.text.toString())
				etMessageText.text.clear()
			}
			btnBack.setOnClickListener { viewModel.navigateBack() }
			btnRate.setOnClickListener { viewModel.rateAnswer(answerId, etRating.text.toString().toInt()) }
		}
	}

	private fun initRV() {
		binding.rvMessages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		binding.rvMessages.adapter = messageAdapter
	}

	private fun render(state: AnswerState) {
		if (state is AnswerState.Content) {
			with(binding) {
				tvTitleTask.text = state.answerInfo.task.taskType
				tvTask.text = state.answerInfo.task.text
				messageAdapter.messages = state.answerInfo.messages
				rvMessages.scrollToPosition(messageAdapter.messages.lastIndex)
				etRating.setText(state.answerInfo.answer.rating.toString())
			}
		}
	}
}