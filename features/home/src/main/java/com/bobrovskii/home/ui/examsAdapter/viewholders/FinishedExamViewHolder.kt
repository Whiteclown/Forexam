package com.bobrovskii.home.ui.examsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.core.toDate
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.home.databinding.PeriodFinishedItemBinding

class FinishedExamViewHolder(
	private val binding: PeriodFinishedItemBinding,
) :
	BaseExamViewHolder(binding.root) {

	fun bind(
		item: Exam,
		onItemClicked: (Int) -> Unit
	) {
		with(binding) {
			//Set data and listeners
			tvTitle.text = item.name
			tvEndTime.text = "Закончился: ${item.end.toDate()}"

			itemView.setOnClickListener { onItemClicked(absoluteAdapterPosition) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): FinishedExamViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = PeriodFinishedItemBinding.inflate(layoutInflater, parent, false)
			return FinishedExamViewHolder(binding)
		}
	}
}