package com.bobrovskii.home.presentation.periodsAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.home.R
import com.bobrovskii.home.presentation.periodsAdapter.viewholders.AllowancePeriodViewHolder
import com.bobrovskii.home.presentation.periodsAdapter.viewholders.ClosedPeriodViewHolder
import com.bobrovskii.home.presentation.periodsAdapter.viewholders.EditPeriodViewHolder
import com.bobrovskii.home.presentation.periodsAdapter.viewholders.FinishedPeriodViewHolder
import com.bobrovskii.home.presentation.periodsAdapter.viewholders.ProgressPeriodViewHolder
import com.bobrovskii.home.presentation.periodsAdapter.viewholders.ReadyPeriodViewHolder

class PeriodsAdapter(
	private val onItemClicked: (Int) -> Unit,
	private val onDeleteClicked: (Int) -> Unit,
) : ListAdapter<Period, RecyclerView.ViewHolder>(PeriodsDiffCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
		when (viewType) {
			R.layout.period_edit_item      -> EditPeriodViewHolder.from(parent)
			R.layout.period_allowance_item -> AllowancePeriodViewHolder.from(parent)
			R.layout.period_ready_item     -> ReadyPeriodViewHolder.from(parent)
			R.layout.period_progress_item  -> ProgressPeriodViewHolder.from(parent)
			R.layout.period_finished_item  -> FinishedPeriodViewHolder.from(parent)
			R.layout.period_closed_item    -> ClosedPeriodViewHolder.from(parent)
			else                           -> throw IllegalArgumentException("Invalid view type")
		}

	override fun getItemViewType(position: Int) = when (getItem(position).state) {
		ExamStates.REDACTION -> R.layout.period_edit_item
		ExamStates.ALLOWANCE -> R.layout.period_allowance_item
		ExamStates.READY     -> R.layout.period_ready_item
		ExamStates.PROGRESS  -> R.layout.period_progress_item
		ExamStates.FINISHED  -> R.layout.period_finished_item
		ExamStates.CLOSED    -> R.layout.period_closed_item
		else                 -> throw IllegalStateException("Unknown view")
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val item = getItem(position)
		when (holder) {
			is EditPeriodViewHolder      -> {
				holder.bind(
					item = item,
					onItemClicked = onItemClicked,
					onDeleteClicked = onDeleteClicked,
				)
			}

			is AllowancePeriodViewHolder -> {
				holder.bind(
					item = item,
					onItemClicked = onItemClicked,
					onDeleteClicked = onDeleteClicked,
				)
			}

			is ReadyPeriodViewHolder     -> {
				holder.bind(item = item) { onItemClicked(item.id) }
			}

			is ProgressPeriodViewHolder  -> {
				holder.bind(
					item = item,
					onItemClicked = onItemClicked,
					onDeleteClicked = onDeleteClicked,
				)
			}

			is FinishedPeriodViewHolder  -> {
				holder.bind(item = item) { onItemClicked(item.id) }
			}

			is ClosedPeriodViewHolder    -> {
				holder.bind(item = item) { onItemClicked(item.id) }
			}

			else                         -> throw IllegalArgumentException("Invalid holder")
		}
	}
}

private class PeriodsDiffCallback : DiffUtil.ItemCallback<Period>() {

	override fun areItemsTheSame(oldItem: Period, newItem: Period) = oldItem.id == newItem.id

	override fun areContentsTheSame(oldItem: Period, newItem: Period) = oldItem == newItem
}