package com.bobrovskii.home.ui.examsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.core.toDate
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.home.databinding.PeriodProgressItemBinding

class ProgressExamViewHolder(
	private val binding: PeriodProgressItemBinding,
) :
	BaseExamViewHolder(binding.root) {

	fun bind(
		item: Exam,
		onItemClicked: (Int) -> Unit,
		onDeleteClicked: (Int) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			tvTitle.text = item.name
			tvStartTime.text = "Идет с ${item.start.toDate()}"

			itemView.setOnClickListener { onItemClicked(item.id) }
			imageButtonDelete.setOnClickListener { onDeleteClicked(item.id) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): ProgressExamViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = PeriodProgressItemBinding.inflate(layoutInflater, parent, false)
			return ProgressExamViewHolder(binding)
		}
	}
}