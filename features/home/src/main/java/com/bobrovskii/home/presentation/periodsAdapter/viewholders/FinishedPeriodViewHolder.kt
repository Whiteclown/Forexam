package com.bobrovskii.home.presentation.periodsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.home.databinding.PeriodFinishedItemBinding

class FinishedPeriodViewHolder(
	private val binding: PeriodFinishedItemBinding,
) :
	BasePeriodViewHolder(binding.root) {

	fun bind(
		item: Period,
		onItemClicked: (Int) -> Unit
	) {
		with(binding) {
			//Set data and listeners
			textViewTitle.text = item.discipline

			itemView.setOnClickListener { onItemClicked(absoluteAdapterPosition) }
		}
	}

	companion object {

		fun from(parent: ViewGroup): FinishedPeriodViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = PeriodFinishedItemBinding.inflate(layoutInflater, parent, false)
			return FinishedPeriodViewHolder(binding)
		}
	}
}