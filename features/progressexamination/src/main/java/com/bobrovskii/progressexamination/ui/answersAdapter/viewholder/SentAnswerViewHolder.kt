package com.bobrovskii.progressexamination.ui.answersAdapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.progressexamination.databinding.ItemSentAnswerBinding

class SentAnswerViewHolder(
	private val binding: ItemSentAnswerBinding,
) : BaseAnswerViewHolder(binding.root) {

	fun bind(
		item: Answer,
		onItemClicked: (Int) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			tvStudentName.text = item.studentName
			tvTaskType.text = item.type

			itemView.setOnClickListener { onItemClicked(item.id) }
			//imageButtonDelete.setOnClickListener { onDeleteClicked(item.id) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): SentAnswerViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = ItemSentAnswerBinding.inflate(layoutInflater, parent, false)
			return SentAnswerViewHolder(binding)
		}
	}
}