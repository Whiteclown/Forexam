package com.bobrovskii.progressexamination.ui.answersAdapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.progressexamination.databinding.ItemRatedAnswerBinding

class RatedAnswerViewHolder(
	private val binding: ItemRatedAnswerBinding,
) : BaseAnswerViewHolder(binding.root) {

	fun bind(
		item: Answer,
		onItemClicked: (Int) -> Unit,
		onItemLongClicked: (Int) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			tvStudentName.text = item.studentName
			tvTaskType.text = "${item.type} ${item.number}"

			itemView.setOnClickListener { onItemClicked(item.id) }
			itemView.setOnLongClickListener {
				onItemLongClicked(item.id)
				true
			}
		}
	}

	companion object {

		fun from(parent: ViewGroup): RatedAnswerViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = ItemRatedAnswerBinding.inflate(layoutInflater, parent, false)
			return RatedAnswerViewHolder(binding)
		}
	}
}