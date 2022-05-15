package com.bobrovskii.home.presentation.periodsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.home.databinding.PeriodReadyItemBinding

class ReadyPeriodViewHolder(
	private val binding: PeriodReadyItemBinding,
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

		fun from(parent: ViewGroup): ReadyPeriodViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = PeriodReadyItemBinding.inflate(layoutInflater, parent, false)
			return ReadyPeriodViewHolder(binding)
		}
	}
}