package com.bobrovskii.accessexamination.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bobrovskii.accessexamination.ui.adapter.viewholders.TicketViewHolder
import com.bobrovskii.exam.domain.entity.Ticket

class TicketsAdapter(
	private val onSemesterRatingChanged: (Int, Int) -> Unit,
	private val onAllowanceChanged: (Int, Boolean) -> Unit,
) : ListAdapter<Ticket, TicketViewHolder>(PeriodsDiffCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder =
		TicketViewHolder.from(parent)

	override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
		val item = getItem(position)
		holder.bind(
			item = item,
			onSemesterRatingChanged = onSemesterRatingChanged,
			onAllowanceChanged = onAllowanceChanged,
		)
	}
}

private class PeriodsDiffCallback : DiffUtil.ItemCallback<Ticket>() {

	override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket) = oldItem.id == newItem.id

	override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket) = oldItem == newItem
}