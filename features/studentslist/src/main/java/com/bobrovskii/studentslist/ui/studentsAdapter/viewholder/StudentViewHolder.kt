package com.bobrovskii.studentslist.ui.studentsAdapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.studentslist.databinding.ItemStudentBinding

class StudentViewHolder(
	private val binding: ItemStudentBinding,
) : BaseStudentViewHolder(binding.root) {

	fun bind(
		item: String,
		onItemClicked: (String) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			tvStudentName.text = item

			itemView.setOnClickListener { onItemClicked(item) }
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