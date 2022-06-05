package com.bobrovskii.home.ui.examsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.core.toDate
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.home.databinding.ItemTimesetExamBinding

class TimesetExamViewHolder(
	private val binding: ItemTimesetExamBinding,
) : BaseExamViewHolder(binding.root) {

	fun bind(
		item: Exam,
		onItemClicked: (Int) -> Unit,
		onDeleteClicked: (Int) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			tvTitle.text = item.name
			tvStartTime.text = item.start.toDate()

			itemView.setOnClickListener { onItemClicked(item.id) }
			imageButtonDelete.setOnClickListener { onDeleteClicked(item.id) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): TimesetExamViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = ItemTimesetExamBinding.inflate(layoutInflater, parent, false)
			return TimesetExamViewHolder(binding)
		}
	}
}