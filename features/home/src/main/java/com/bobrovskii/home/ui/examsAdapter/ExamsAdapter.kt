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
			R.layout.item_exam_edit     -> EditExamViewHolder.from(parent)
			R.layout.item_timeset_exam  -> TimesetExamViewHolder.from(parent)
			R.layout.item_exam_ready    -> ReadyExamViewHolder.from(parent)
			R.layout.item_exam_progress -> ProgressExamViewHolder.from(parent)
			R.layout.item_exam_finished -> FinishedExamViewHolder.from(parent)
			R.layout.item_exam_closed   -> ClosedExamViewHolder.from(parent)
			else                        -> throw IllegalArgumentException("Invalid view type")
		}

	override fun getItemViewType(position: Int) = when (getItem(position).state) {
		ExamStates.REDACTION -> R.layout.item_exam_edit
		ExamStates.TIME_SET  -> R.layout.item_timeset_exam
		ExamStates.READY     -> R.layout.item_exam_ready
		ExamStates.PROGRESS  -> R.layout.item_exam_progress
		ExamStates.FINISHED  -> R.layout.item_exam_finished
		ExamStates.CLOSED    -> R.layout.item_exam_closed
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
				holder.bind(item = item) { onItemClicked(item.id) }
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