package com.bobrovskii.home.ui.examsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.home.databinding.PeriodReadyItemBinding

class ReadyExamViewHolder(
	private val binding: PeriodReadyItemBinding,
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

		fun from(parent: ViewGroup): ReadyExamViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = PeriodReadyItemBinding.inflate(layoutInflater, parent, false)
			return ReadyExamViewHolder(binding)
		}
	}
}