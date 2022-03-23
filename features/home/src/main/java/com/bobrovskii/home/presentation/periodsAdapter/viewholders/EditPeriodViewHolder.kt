package com.bobrovskii.home.presentation.periodsAdapter.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.home.databinding.PeriodEditItemBinding

class EditPeriodViewHolder(private val binding: PeriodEditItemBinding) :
	BasePeriodViewHolder(binding.root) {

	fun bind(
		item: Period
	) {
		period = item
		with(binding) {
			//Set data and listeners
			textViewTitle.text = item.discipline
		}
	}

	companion object {

		fun from(parent: ViewGroup): EditPeriodViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = PeriodEditItemBinding.inflate(layoutInflater, parent, false)
			return EditPeriodViewHolder(binding)
		}
	}
}