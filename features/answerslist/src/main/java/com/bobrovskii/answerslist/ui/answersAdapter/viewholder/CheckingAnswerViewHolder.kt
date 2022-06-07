package com.bobrovskii.answerslist.ui.answersAdapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.answerslist.databinding.ItemCheckingAnswerBinding
import com.bobrovskii.exam.domain.entity.Answer

class CheckingAnswerViewHolder(
	private val binding: ItemCheckingAnswerBinding,
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

		fun from(parent: ViewGroup): CheckingAnswerViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = ItemCheckingAnswerBinding.inflate(layoutInflater, parent, false)
			return CheckingAnswerViewHolder(binding)
		}
	}
}