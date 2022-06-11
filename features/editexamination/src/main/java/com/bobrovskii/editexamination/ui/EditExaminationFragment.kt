package com.bobrovskii.editexamination.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.IOnBackPressed
import com.bobrovskii.editexamination.R
import com.bobrovskii.editexamination.databinding.FragmentEditExaminationBinding
import com.bobrovskii.editexamination.presentation.EditExaminationAction
import com.bobrovskii.editexamination.presentation.EditExaminationState
import com.bobrovskii.editexamination.presentation.EditExaminationViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditExaminationFragment : Fragment(R.layout.fragment_edit_examination), IOnBackPressed {

	private val viewModel: EditExaminationViewModel by viewModels()

	private var _binding: FragmentEditExaminationBinding? = null
	private val binding get() = _binding!!

	private val examId: Int by lazy {
		arguments?.getInt(EXAM_ID) ?: throw IllegalStateException("no examId")
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
		_binding = FragmentEditExaminationBinding.bind(view)
		initListeners()
		lifecycleScope.launch {
			viewModel.actions.collect { handleAction(it) }
		}
		viewModel.loadData(examId)
		viewModel.state.onEach(::render).launchIn(viewModel.viewModelScope)
	}

	private fun initListeners() {
		with(binding) {
			btnSave.setOnClickListener {
				viewModel.updateExam(
					binding.etName.text.toString(),
					binding.acDiscipline.text.toString(),
					binding.cgGroups.checkedChipId,
					!binding.swRepass.isChecked,
					false
				)
			}
			btnSaveAndChangeState.setOnClickListener {
				viewModel.updateExam(
					binding.etName.text.toString(),
					binding.acDiscipline.text.toString(),
					binding.cgGroups.checkedChipId,
					!binding.swRepass.isChecked,
					true
				)
			}
			imageButtonBack.setOnClickListener { viewModel.navigateBack() }
			swRepass.setOnCheckedChangeListener { _, isChecked ->
				binding.tvGroups.visibility = if (isChecked) View.INVISIBLE else View.VISIBLE
				binding.cgGroups.visibility = if (isChecked) View.INVISIBLE else View.VISIBLE
			}
		}
	}

	private fun render(state: EditExaminationState) {
		if (state is EditExaminationState.Content) {
			binding.etName.setText(state.exam.name)
			binding.swRepass.isChecked = !state.exam.oneGroup

			val adapterDiscipline = ArrayAdapter<String>(binding.acDiscipline.context, R.layout.item_discipline)
			adapterDiscipline.clear()
			state.disciplines.let { list ->
				adapterDiscipline.addAll(list.map { it.name })
			}
			state.selectedDiscipline.let {
				binding.acDiscipline.setText(it.name)
			}
			binding.acDiscipline.setAdapter(adapterDiscipline)

			binding.cgGroups.removeAllViews()
			state.groups.map {
				val chip = Chip(binding.cgGroups.context)
				chip.text = it.name
				chip.id = it.id
				chip.isCheckable = true
				chip.isChecked = state.selectedGroup?.id == it.id
				binding.cgGroups.addView(chip)
			}
			binding.loadingView.root.visibility = if (state is EditExaminationState.Loading) View.VISIBLE else View.GONE
		}
	}

	private fun handleAction(action: EditExaminationAction) {
		when (action) {
			is EditExaminationAction.ShowError -> {
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

	override fun onBackPressed() = true
}