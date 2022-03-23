package com.bobrovskii.home.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bobrovskii.home.R
import com.bobrovskii.home.databinding.DialogFragmentAddExamBinding
import com.bobrovskii.home.presentation.AddExamViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddExamDialogFragment : DialogFragment(R.layout.dialog_fragment_add_exam) {

	private lateinit var binding: DialogFragmentAddExamBinding

	private val viewModel: AddExamViewModel by viewModels()

	override fun onStart() {
		super.onStart()
		if (dialog != null) {
			val width = ViewGroup.LayoutParams.MATCH_PARENT
			val height = ViewGroup.LayoutParams.MATCH_PARENT
			dialog!!.window?.setLayout(width, height)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding = DialogFragmentAddExamBinding.bind(view)
		initListeners()
		initSpinners()
	}

	private fun initListeners() {
		var disciplineId = -1
		var examRuleId = -1
		binding.imageButtonBack.setOnClickListener {
			viewModel.navigateBack()
		}
		binding.autoCompleteDiscipline.setOnItemClickListener { _, _, i, _ ->
			disciplineId = i
			viewModel.loadExamRulesAndGroups(disciplineId)
			binding.insteadGroupsChips.visibility = View.INVISIBLE
		}
		binding.autoCompleteExamRule.setOnItemClickListener { _, _, i, _ ->
			examRuleId = i
		}
		binding.buttonAddExam.setOnClickListener {
			viewModel.addExam(disciplineId, examRuleId, binding.chipGroup.checkedChipIds, binding.editTextStartDate.editableText.toString())
		}
		binding.editTextStartDate.setOnClickListener {
			var calendar: Calendar = Calendar.getInstance()
			val day = calendar.get(Calendar.DAY_OF_MONTH)
			val month = calendar.get(Calendar.MONTH)
			val year = calendar.get(Calendar.YEAR)
			context?.let { it1 -> DatePickerDialog(
				it1,
				DatePickerDialog.OnDateSetListener { datePicker, yearPicked, monthPicked, dayPicked ->
					calendar = Calendar.getInstance()
					val hour = calendar.get(Calendar.HOUR)
					val minute = calendar.get(Calendar.MINUTE)
					TimePickerDialog(
						context,
						TimePickerDialog.OnTimeSetListener { timePicker, hourPicked, minutePicked ->
							val monthNormal = monthPicked + 1
							val monthStr = if (monthNormal < 10) "0$monthNormal" else monthNormal.toString()
							val dayStr  = if (dayPicked < 10) "0$dayPicked" else dayPicked.toString()
							val hourStr = if (hourPicked < 10) "0$hourPicked" else hourPicked.toString()
							val minuteStr = if (minutePicked < 10) "0$minutePicked" else minutePicked.toString()
							binding.editTextStartDate.setText(getString(R.string.date_picker_text, yearPicked, monthStr, dayStr, hourStr, minuteStr))
						},
						hour,
						minute,
						DateFormat.is24HourFormat(context)
					).show()
				},
				year,
				month,
				day
			).show() }
		}
	}

	private fun initSpinners() {
		val adapterDiscipline = ArrayAdapter<String>(requireContext(), R.layout.item_discipline)
		viewModel.disciplinesLiveData.observe(viewLifecycleOwner) { list ->
			adapterDiscipline.clear()
			adapterDiscipline.addAll(list.map { it.name })
		}
		binding.autoCompleteDiscipline.setAdapter(adapterDiscipline)

		val adapterExamRule = ArrayAdapter<String>(requireContext(), R.layout.item_discipline)
		viewModel.examRulesLiveData.observe(viewLifecycleOwner) { list ->
			adapterExamRule.clear()
			adapterExamRule.addAll(list.map { it.name })
		}
		binding.autoCompleteExamRule.setAdapter(adapterExamRule)

		viewModel.groupsLiveData.observe(viewLifecycleOwner) { list ->
			list.map {
				val chip = Chip(context)
				chip.text = it.name
				chip.id = it.id
				chip.isCheckable = true
				binding.chipGroup.addView(chip)
			}
		}
	}
}