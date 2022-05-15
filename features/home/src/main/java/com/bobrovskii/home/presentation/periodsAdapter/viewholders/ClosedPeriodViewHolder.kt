package com.bobrovskii.home.presentation.periodsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.home.databinding.PeriodClosedItemBinding

class ClosedPeriodViewHolder(
	private val binding: PeriodClosedItemBinding,
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

		fun from(parent: ViewGroup): ClosedPeriodViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = PeriodClosedItemBinding.inflate(layoutInflater, parent, false)
			return ClosedPeriodViewHolder(binding)
		}
	}
}