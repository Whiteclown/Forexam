package com.bobrovskii.home.ui.examsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.home.databinding.PeriodEditItemBinding

class EditExamViewHolder(
	private val binding: PeriodEditItemBinding,
) : BaseExamViewHolder(binding.root) {

	fun bind(
		item: Exam,
		onItemClicked: (Int) -> Unit,
		onDeleteClicked: (Int) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			tvTitle.text = item.name

			itemView.setOnClickListener { onItemClicked(item.id) }
			imageButtonDelete.setOnClickListener { onDeleteClicked(item.id) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): EditExamViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = PeriodEditItemBinding.inflate(layoutInflater, parent, false)
			return EditExamViewHolder(binding)
		}
	}
}