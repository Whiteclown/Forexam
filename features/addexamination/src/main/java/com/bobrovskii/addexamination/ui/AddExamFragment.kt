package com.bobrovskii.addexamination.ui

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
import com.bobrovskii.addexamination.R
import com.bobrovskii.addexamination.databinding.FragmentAddExamBinding
import com.bobrovskii.addexamination.presentation.AddExamAction
import com.bobrovskii.addexamination.presentation.AddExamState
import com.bobrovskii.addexamination.presentation.AddExamViewModel
import com.bobrovskii.core.IOnBackPressed
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddExamFragment : Fragment(R.layout.fragment_add_exam), IOnBackPressed {

	private var _binding: FragmentAddExamBinding? = null
	private val binding get() = _binding!!

	private val viewModel: AddExamViewModel by viewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		_binding = FragmentAddExamBinding.bind(view)
		initListeners()
		viewModel.state.onEach(::render).launchIn(viewModel.viewModelScope)
		lifecycleScope.launch {
			viewModel.actions.collect { handleAction(it) }
		}
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

	private fun render(state: AddExamState) {
		if (state is AddExamState.Content) initSpinners(state)
		binding.loadingView.root.visibility = if (state is AddExamState.Loading) View.VISIBLE else View.GONE
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

	private fun handleAction(action: AddExamAction) {
		when (action) {
			is AddExamAction.ShowError -> {
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