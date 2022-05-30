package com.bobrovskii.home.ui.examsAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.home.R
import com.bobrovskii.home.ui.examsAdapter.viewholders.ClosedExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.EditExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.FinishedExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.ProgressExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.ReadyExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.TimesetExamViewHolder

class ExamsAdapter(
	private val onItemClicked: (Int) -> Unit,
	private val onDeleteClicked: (Int) -> Unit,
) : ListAdapter<Exam, RecyclerView.ViewHolder>(ExamsDiffCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
		when (viewType) {
			R.layout.period_edit_item     -> EditExamViewHolder.from(parent)
			R.layout.period_timeset_item  -> TimesetExamViewHolder.from(parent)
			R.layout.period_ready_item    -> ReadyExamViewHolder.from(parent)
			R.layout.period_progress_item -> ProgressExamViewHolder.from(parent)
			R.layout.period_finished_item -> FinishedExamViewHolder.from(parent)
			R.layout.period_closed_item   -> ClosedExamViewHolder.from(parent)
			else                          -> throw IllegalArgumentException("Invalid view type")
		}

	override fun getItemViewType(position: Int) = when (getItem(position).state) {
		ExamStates.REDACTION -> R.layout.period_edit_item
		ExamStates.TIME_SET  -> R.layout.period_timeset_item
		ExamStates.READY     -> R.layout.period_ready_item
		ExamStates.PROGRESS  -> R.layout.period_progress_item
		ExamStates.FINISHED  -> R.layout.period_finished_item
		ExamStates.CLOSED    -> R.layout.period_closed_item
		else                 -> throw IllegalStateException("Unknown view")
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val item = getItem(position)
		when (holder) {
			is EditExamViewHolder     -> {
				holder.bind(
					item = item,
					onItemClicked = onItemClicked,
					onDeleteClicked = onDeleteClicked,
				)
			}

			is TimesetExamViewHolder  -> {
				holder.bind(
					item = item,
					onItemClicked = onItemClicked,
					onDeleteClicked = onDeleteClicked,
				)
			}

			is ReadyExamViewHolder    -> {
				holder.bind(item = item) { onItemClicked(item.id) }
			}

			is ProgressExamViewHolder -> {
				holder.bind(
					item = item,
					onItemClicked = onItemClicked,
					onDeleteClicked = onDeleteClicked,
				)
			}

			is FinishedExamViewHolder -> {
				holder.bind(item = item) { onItemClicked(item.id) }
			}

			is ClosedExamViewHolder   -> {
				holder.bind(item = item) { onItemClicked(item.id) }
			}

			else                         -> throw IllegalArgumentException("Invalid holder")
		}
	}
}

private class ExamsDiffCallback : DiffUtil.ItemCallback<Exam>() {

	override fun areItemsTheSame(oldItem: Exam, newItem: Exam) = oldItem.id == newItem.id

	override fun areContentsTheSame(oldItem: Exam, newItem: Exam) = oldItem == newItem
}