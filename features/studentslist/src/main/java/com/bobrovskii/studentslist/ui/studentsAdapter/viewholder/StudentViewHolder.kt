package com.bobrovskii.studentslist.ui.studentsAdapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Ticket
import com.bobrovskii.studentslist.databinding.ItemStudentBinding

class StudentViewHolder(
	private val binding: ItemStudentBinding,
) : BaseStudentViewHolder(binding.root) {

	fun bind(
		item: Ticket,
		onItemClicked: (String) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			val sumRating = item.semesterRating + item.examRating
			tvStudentName.text = item.studentName
			tvSemesterRating.text = item.semesterRating.toString()
			tvExamRating.text = item.examRating.toString()
			tvSumRating.text = sumRating.toString()
			tvMark.text = when (sumRating) {
				in 0..49   -> "2"
				in 50..72  -> "3"
				in 73..85  -> "4"
				in 86..100 -> "5"

				else       -> "-1"
			}

			itemView.setOnClickListener { onItemClicked(item.studentName) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): StudentViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = ItemStudentBinding.inflate(layoutInflater, parent, false)
			return StudentViewHolder(binding)
		}
	}
}