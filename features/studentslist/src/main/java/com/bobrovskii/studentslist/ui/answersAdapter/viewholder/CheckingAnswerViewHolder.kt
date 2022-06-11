package com.bobrovskii.studentslist.ui.answersAdapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.studentslist.databinding.ItemCheckingAnswerBinding

class CheckingAnswerViewHolder(
	private val binding: ItemCheckingAnswerBinding,
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

		fun from(parent: ViewGroup): CheckingAnswerViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = ItemCheckingAnswerBinding.inflate(layoutInflater, parent, false)
			return CheckingAnswerViewHolder(binding)
		}
	}
}