package com.bobrovskii.studentslist.ui.answersAdapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.studentslist.databinding.ItemInProgressAnswerBinding

class InProgressAnswerViewHolder(
	private val binding: ItemInProgressAnswerBinding,
) : BaseAnswerViewHolder(binding.root) {

	fun bind(
		item: Answer,
		onItemClicked: (Int) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			tvStudentName.text = item.studentName
			tvTaskType.text = "${item.type} ${item.number}"

			itemView.setOnClickListener { onItemClicked(item.id) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): InProgressAnswerViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = ItemInProgressAnswerBinding.inflate(layoutInflater, parent, false)
			return InProgressAnswerViewHolder(binding)
		}
	}
}