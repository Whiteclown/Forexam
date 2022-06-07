package ui

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobrovskii.answer.R
import com.bobrovskii.answer.databinding.FragmentAnswerBinding
import com.bobrovskii.core.AnswerStates
import com.bobrovskii.core.NOTIFICATION_MESSAGE_FILTER
import com.bobrovskii.core.OPEN_DOCUMENT_REQUEST_CODE
import com.bobrovskii.core.OpenFileResult
import com.bobrovskii.core.allSupportedDocumentsTypesToExtensions
import com.bobrovskii.core.tryHandleOpenDocumentResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import presentation.AnswerAction
import presentation.AnswerState
import presentation.AnswerViewModel
import ui.messageAdapter.MessageAdapter

@AndroidEntryPoint
class AnswerFragment : Fragment(R.layout.fragment_answer) {

	private var _binding: FragmentAnswerBinding? = null
	private val binding get() = _binding!!

	private val viewModel: AnswerViewModel by viewModels()

	private var readPermissionGranted = false
	private var writePermissionGranted = false
	private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

	private val answerId: Int by lazy {
		arguments?.getInt(ANSWER_ID) ?: throw IllegalStateException("no answer id")
	}

	private val isClosed: Boolean by lazy {
		arguments?.getBoolean(IS_CLOSED) ?: throw IllegalStateException("no exam state")
	}

	private val messageAdapter = MessageAdapter(
		onItemClicked = {
			viewModel.showArtefact(it)
		}
	)

	private val notificationReceiver = object : BroadcastReceiver() {

		override fun onReceive(context: Context?, intent: Intent?) {
			if (intent != null) {
				viewModel.refresh(answerId, intent.getIntExtra("accountId", -1))
			}
		}
	}

	companion object {

		private const val ANSWER_ID = "ANSWER_ID"
		private const val IS_CLOSED = "IS_CLOSED"

		fun createBundle(answerId: Int, isClosed: Boolean) =
			Bundle().apply {
				putInt(ANSWER_ID, answerId)
				putBoolean(IS_CLOSED, isClosed)
			}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentAnswerBinding.bind(view)
		initRV()
		initListeners()
		lifecycleScope.launch {
			viewModel.actions.collect { handleAction(it) }
		}
		viewModel.state.onEach(::render).launchIn(viewModel.viewModelScope)
		permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
			readPermissionGranted = it[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
			writePermissionGranted = it[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: writePermissionGranted
		}
		updateOrRequestPermissions()
	}

	private fun initListeners() {
		with(binding) {
			btnSendMessage.setOnClickListener {
				viewModel.sendMessage(answerId, etMessageText.text.toString())
				llArtefact.visibility = View.GONE
				etMessageText.text.clear()
			}
			btnBack.setOnClickListener { viewModel.navigateBack() }
			btnRate.setOnClickListener { viewModel.rateAnswer(answerId, etRating.text.toString().toInt()) }
			btnAttachFile.setOnClickListener {
				val supportedMimeTypes = allSupportedDocumentsTypesToExtensions.keys.toTypedArray()
				try {
					Intent(Intent.ACTION_GET_CONTENT).apply {
						addCategory(Intent.CATEGORY_OPENABLE)
						type = "*/*"
						putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes)
						startActivityForResult(this, OPEN_DOCUMENT_REQUEST_CODE)
					}
				} catch (openableException: ActivityNotFoundException) {
					context?.let {
						AlertDialog
							.Builder(it)
							.setTitle("Ошибка")
							.setMessage("Не найдено приложение для открытия данного типа файлов")
							.setNeutralButton("Ок") { _, _ -> }
							.show()
					}
				}
			}
			btnDetach.setOnClickListener {
				viewModel.detachArtefact()
			}
			btnReturn.setOnClickListener {
				viewModel.returnAnswer(answerId)
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		when (val result = tryHandleOpenDocumentResult(requestCode, resultCode, data)) {
			OpenFileResult.DifferentResult, OpenFileResult.OpenFileWasCancelled -> {}

			OpenFileResult.ErrorOpeningFile                                     -> Log.e("TAG", "error opening file")

			is OpenFileResult.FileWasOpened                                     -> {
				viewModel.attachArtefact(result.fileName, result.content)
			}
		}
	}

	private fun initRV() {
		binding.rvMessages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
		binding.rvMessages.adapter = messageAdapter
	}

	private fun render(state: AnswerState) {
		if (isClosed) {
			with(binding) {
				llLowerPanel.visibility = View.GONE
				btnRate.visibility = View.GONE
				btnReturn.visibility = View.GONE
				etRating.apply {
					isEnabled = false
					setTextColor(Color.BLACK)
				}
			}
		}
		if (state is AnswerState.Content) {
			with(binding) {
				tvTitleTask.text = state.answerInfo.task.taskType
				tvTask.text = state.answerInfo.task.text
				messageAdapter.messages = state.answerInfo.messages
				rvMessages.scrollToPosition(state.answerInfo.messages.lastIndex)
				etRating.setText(state.answerInfo.answer.rating.toString())
				state.metaData?.let {
					tvArtefactName.text = it.fullName.take(20)
					llArtefact.visibility = View.VISIBLE
				} ?: run {
					llArtefact.visibility = View.GONE
				}
				state.file?.let {
					Intent(Intent.ACTION_VIEW, it).apply {
						flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
						startActivity(this)
					}
					viewModel.clearBuff()
				}
				btnRate.isEnabled = state.answerInfo.answer.state == AnswerStates.CHECKING
				if (state.answerInfo.answer.state == AnswerStates.CHECKING
					|| state.answerInfo.answer.state == AnswerStates.RATED
				) {
					btnReturn.isEnabled = true
					btnReturn.backgroundTintList = ColorStateList.valueOf(Color.rgb(176, 0, 32))
				} else {
					btnReturn.isEnabled = false
					btnReturn.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
				}
			}
		}
		binding.loadingView.root.visibility = if (state is AnswerState.Loading) View.VISIBLE else View.GONE
	}

	private fun updateOrRequestPermissions() {
		context?.let {
			val hasReadPermission = ContextCompat.checkSelfPermission(
				it,
				Manifest.permission.READ_EXTERNAL_STORAGE
			) == PackageManager.PERMISSION_GRANTED
			val hasWritePermission = ContextCompat.checkSelfPermission(
				it,
				Manifest.permission.WRITE_EXTERNAL_STORAGE
			) == PackageManager.PERMISSION_GRANTED
			val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
			readPermissionGranted = hasReadPermission
			writePermissionGranted = hasWritePermission || minSdk29
			val permissionsToRequest = mutableListOf<String>()
			if (!writePermissionGranted) {
				permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
			}
			if (!readPermissionGranted) {
				permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
			}
			if (permissionsToRequest.isNotEmpty()) {
				permissionsLauncher.launch(permissionsToRequest.toTypedArray())
			}
		}
	}

	private fun handleAction(action: AnswerAction) {
		when (action) {
			is AnswerAction.ShowError -> {
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

	override fun onResume() {
		super.onResume()
		requireActivity().registerReceiver(notificationReceiver, NOTIFICATION_MESSAGE_FILTER)
		viewModel.loadData(answerId)
	}

	override fun onPause() {
		super.onPause()
		requireActivity().unregisterReceiver(notificationReceiver)
	}
}