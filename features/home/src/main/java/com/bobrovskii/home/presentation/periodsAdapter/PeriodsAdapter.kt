package com.bobrovskii.home.presentation.periodsAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.home.R
import com.bobrovskii.home.presentation.periodsAdapter.viewholders.EditPeriodViewHolder

class PeriodsAdapter : ListAdapter<Period, RecyclerView.ViewHolder>(PeriodsDiffCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
		when (viewType) {
			R.layout.period_edit_item -> EditPeriodViewHolder.from(parent)
			else                      -> throw IllegalArgumentException("Invalid view type")
		}

	override fun getItemViewType(position: Int) = when (getItem(position).state) {
		"REDACTION" -> R.layout.period_edit_item
		else        -> throw IllegalStateException("Unknown view")
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val item = getItem(position)
		when (holder) {
			is EditPeriodViewHolder -> holder.bind(
				item = item
			)
			else                    -> throw IllegalArgumentException("Invalid holder")
		}
	}
}

private class PeriodsDiffCallback : DiffUtil.ItemCallback<Period>() {

	override fun areItemsTheSame(oldItem: Period, newItem: Period) = oldItem.id == newItem.id

	override fun areContentsTheSame(oldItem: Period, newItem: Period) = oldItem == newItem
}