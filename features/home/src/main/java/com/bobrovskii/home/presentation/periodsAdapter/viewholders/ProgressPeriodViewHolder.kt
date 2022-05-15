package com.bobrovskii.home.presentation.periodsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.home.databinding.PeriodProgressItemBinding

class ProgressPeriodViewHolder(
	private val binding: PeriodProgressItemBinding,
) :
	BasePeriodViewHolder(binding.root) {

	fun bind(
		item: Period,
		onItemClicked: (Int) -> Unit,
		onDeleteClicked: (Int) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			textViewTitle.text = item.discipline

			//itemView.setOnClickListener { onItemClicked(item.examId) }
			imageButtonDelete.setOnClickListener { onDeleteClicked(item.examId) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): ProgressPeriodViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = PeriodProgressItemBinding.inflate(layoutInflater, parent, false)
			return ProgressPeriodViewHolder(binding)
		}
	}
}