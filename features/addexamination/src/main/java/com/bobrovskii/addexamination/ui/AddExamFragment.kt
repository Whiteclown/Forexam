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
		viewModel.loadData()
	}

	private fun initListeners() {
		with(binding) {
			btnAddExam.setOnClickListener {
				viewModel.addExam(
					binding.etName.text.toString(),
					binding.acDiscipline.text.toString(),
					binding.cgGroups.checkedChipId,
					!binding.swRepass.isChecked
				)
			}
			imageButtonBack.setOnClickListener { viewModel.navigateBack() }
			swRepass.setOnCheckedChangeListener { _, isChecked ->
				binding.tvGroups.visibility = if (isChecked) View.INVISIBLE else View.VISIBLE
				binding.cgGroups.visibility = if (isChecked) View.INVISIBLE else View.VISIBLE
			}
		}
	}

	private fun renderState(state: AddExamState) {
		when (state) {
			is AddExamState.Initial -> {
				with(binding) {
					loadingView.root.visibility = View.VISIBLE
					mainLayout.visibility = View.GONE
				}
			}

			is AddExamState.Loading -> {
				with(binding) {
					loadingView.root.visibility = View.VISIBLE
					mainLayout.visibility = View.GONE
				}
			}

			is AddExamState.Content -> {
				with(binding) {
					mainLayout.visibility = View.VISIBLE
					initSpinners(state)
					loadingView.root.visibility = View.GONE
				}
			}
		}
	}

	private fun initSpinners(content: AddExamState.Content) {
		val adapterDiscipline = ArrayAdapter<String>(binding.acDiscipline.context, R.layout.item_discipline)
		adapterDiscipline.clear()
		content.disciplines.let { list ->
			adapterDiscipline.addAll(list.map { it.name })
		}
		binding.acDiscipline.setAdapter(adapterDiscipline)

		binding.cgGroups.removeAllViews()
		content.groups.map {
			val chip = Chip(binding.cgGroups.context)
			chip.text = it.name
			chip.id = it.id
			chip.isCheckable = true
			binding.cgGroups.addView(chip)
		}
	}
}