package com.bobrovskii.editexamination.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.bobrovskii.core.ShowDateAndTimePicker
import com.bobrovskii.core.toDate
import com.bobrovskii.editexamination.R
import com.bobrovskii.editexamination.databinding.FragmentEditExaminationBinding
import com.bobrovskii.editexamination.presentation.EditExaminationState
import com.bobrovskii.editexamination.presentation.EditExaminationViewModel
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditExaminationFragment : Fragment(R.layout.fragment_edit_examination) {

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
		viewModel.loadData(examId)
		viewModel.state.onEach(::render).launchIn(viewModel.viewModelScope)
	}

	private fun initListeners() {
		with(binding) {
			autoCompleteDiscipline.setOnItemClickListener { _, _, i, _ ->
				viewModel.loadExamRulesAndGroups(i)
			}
			autoCompleteExamRule.setOnItemClickListener { _, _, i, _ ->
				viewModel.setSelectedExamRule(i)
			}
			buttonSave.setOnClickListener {
				viewModel.updateExam(binding.chipGroup.checkedChipIds, binding.editTextStartDate.editableText.toString(), false)
			}
			buttonSaveAndChangeState.setOnClickListener {
				viewModel.updateExam(binding.chipGroup.checkedChipIds, binding.editTextStartDate.editableText.toString(), true)
			}
			editTextStartDate.setOnClickListener { context?.let { it1 -> ShowDateAndTimePicker(context = it1, view = binding.editTextStartDate) } }
			imageButtonBack.setOnClickListener { viewModel.navigateBack() }
		}
	}

	private fun render(state: EditExaminationState) {
		when (state) {
			is EditExaminationState.Initial -> {}

			is EditExaminationState.Loading -> renderLoadingState()

			is EditExaminationState.Content -> {
				initSpinners(state)
			}
		}
	}

	private fun renderLoadingState() {

	}

	private fun renderContentState(
		disciplines: List<Discipline>?,
		examRules: List<ExamRule>?,
		groups: List<Group>?,
		selectedDiscipline: Discipline?,
		selectedExamRule: ExamRule?,
	) {

	}

	private fun initSpinners(content: EditExaminationState.Content) {
		val adapterDiscipline = ArrayAdapter<String>(binding.autoCompleteDiscipline.context, R.layout.item_discipline)
		adapterDiscipline.clear()
		content.disciplines?.let { list ->
			adapterDiscipline.addAll(list.map { it.name })
		}
		content.selectedDiscipline?.let {
			binding.autoCompleteDiscipline.setText(it.name)
		} ?: binding.autoCompleteExamRule.setText("Пожалуйста, выберите дисциплину!")
		binding.autoCompleteDiscipline.setAdapter(adapterDiscipline)

		val adapterExamRule = ArrayAdapter<String>(binding.autoCompleteExamRule.context, R.layout.item_discipline)
		adapterExamRule.clear()
		content.examRules?.let { list ->
			adapterExamRule.addAll(list.map { it.name })
		}
		content.selectedExamRule?.let {
			binding.autoCompleteExamRule.setText(it.name)
		}
		binding.autoCompleteExamRule.setAdapter(adapterExamRule)

		content.selectedStartTime?.let {
			binding.editTextStartDate.setText(it.toDate())
		}

		binding.chipGroup.removeAllViews()
		content.groups?.map {
			val chip = Chip(binding.chipGroup.context)
			chip.text = it.name
			chip.id = it.id
			chip.isCheckable = true
			chip.isChecked = content.selectedGroups?.contains(chip.id) ?: false
			binding.chipGroup.addView(chip)
		}
	}
}