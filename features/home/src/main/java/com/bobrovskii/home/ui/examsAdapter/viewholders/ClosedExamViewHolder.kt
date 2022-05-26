package com.bobrovskii.home.ui.examsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.home.databinding.PeriodClosedItemBinding

class ClosedExamViewHolder(
	private val binding: PeriodClosedItemBinding,
) :
	BaseExamViewHolder(binding.root) {

	fun bind(
		item: Exam,
		onItemClicked: (Int) -> Unit
	) {
		with(binding) {
			//Set data and listeners
			tvTitle.text = item.name

			itemView.setOnClickListener { onItemClicked(absoluteAdapterPosition) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): ClosedExamViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = PeriodClosedItemBinding.inflate(layoutInflater, parent, false)
			return ClosedExamViewHolder(binding)
		}
	}
}