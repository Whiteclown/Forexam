package com.bobrovskii.editexamination.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.bobrovskii.editexamination.R
import com.bobrovskii.editexamination.databinding.FragmentEditExaminationBinding
import com.bobrovskii.editexamination.presentation.EditExaminationState
import com.bobrovskii.editexamination.presentation.EditExaminationViewModel
import com.bobrovskii.exam.domain.entity.Discipline
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
		groups: List<Group>?,
		selectedDiscipline: Discipline?,
	) {

	}

	private fun initSpinners(content: EditExaminationState.Content) {
		binding.etName.setText(content.exam.name)
		binding.swRepass.isChecked = !content.exam.oneGroup

		val adapterDiscipline = ArrayAdapter<String>(binding.acDiscipline.context, R.layout.item_discipline)
		adapterDiscipline.clear()
		content.disciplines.let { list ->
			adapterDiscipline.addAll(list.map { it.name })
		}
		content.selectedDiscipline.let {
			binding.acDiscipline.setText(it.name)
		}
		binding.acDiscipline.setAdapter(adapterDiscipline)

		binding.cgGroups.removeAllViews()
		content.groups.map {
			val chip = Chip(binding.cgGroups.context)
			chip.text = it.name
			chip.id = it.id
			chip.isCheckable = true
			chip.isChecked = content.selectedGroup?.id == it.id
			binding.cgGroups.addView(chip)
		}
	}
}