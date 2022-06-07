package com.bobrovskii.answerslist.ui.answersAdapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.answerslist.databinding.ItemSentAnswerBinding
import com.bobrovskii.exam.domain.entity.Answer

class SentAnswerViewHolder(
	private val binding: ItemSentAnswerBinding,
) : BaseAnswerViewHolder(binding.root) {

	fun bind(
		item: Answer,
		onItemClicked: (Int) -> Unit,
		onItemLongClicked: (Int, String) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			tvStudentName.text = item.studentName
			tvTaskType.text = "${item.type} ${item.number}"

			itemView.setOnClickListener { onItemClicked(item.id) }
			item.studentName?.let { studentName ->
				itemView.setOnLongClickListener {
					onItemLongClicked(item.studentRatingId, studentName)
					true
				}
			}
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