package com.bobrovskii.addexamination.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.bobrovskii.addexamination.R
import com.bobrovskii.addexamination.databinding.FragmentAddExamBinding
import com.bobrovskii.addexamination.presentation.AddExamState
import com.bobrovskii.addexamination.presentation.AddExamViewModel
import com.bobrovskii.core.ShowDateAndTimePicker
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddExamFragment : Fragment(R.layout.fragment_add_exam) {

	private var _binding: FragmentAddExamBinding? = null
	private val binding get() = _binding!!

	private val viewModel: AddExamViewModel by viewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentAddExamBinding.bind(view)
		initListeners()
		viewModel.state.onEach(::renderState).launchIn(viewModel.viewModelScope)
		viewModel.loadDisciplines()
	}

	private fun initListeners() {
		with(binding) {
			autoCompleteDiscipline.setOnItemClickListener { _, _, i, _ ->
				viewModel.loadExamRulesAndGroups(i)
				insteadGroupsChips.visibility = View.GONE
			}
			autoCompleteExamRule.setOnItemClickListener { _, _, i, _ ->
				viewModel.setSelectedExamRule(i)
			}
			buttonAddExam.setOnClickListener {
				viewModel.addExam(binding.chipGroup.checkedChipIds, binding.editTextStartDate.editableText.toString())
			}
			editTextStartDate.setOnClickListener { context?.let { it1 -> ShowDateAndTimePicker(context = it1, view = binding.editTextStartDate) } }
			imageButtonBack.setOnClickListener { viewModel.navigateBack() }
		}
	}

	private fun renderState(state: AddExamState) {
		when (state) {
			is AddExamState.Initial -> {}

			is AddExamState.Loading -> {}

			is AddExamState.Content -> {
				initSpinners(state)
			}
		}
	}

	private fun initSpinners(content: AddExamState.Content) {
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

		binding.chipGroup.removeAllViews()
		content.groups?.map {
			val chip = Chip(binding.chipGroup.context)
			chip.text = it.name
			chip.id = it.id
			chip.isCheckable = true
			binding.chipGroup.addView(chip)
		}
	}
}