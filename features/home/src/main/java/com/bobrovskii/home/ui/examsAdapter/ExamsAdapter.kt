package com.bobrovskii.home.ui.examsAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.home.R
import com.bobrovskii.home.ui.examsAdapter.viewholders.BaseExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.ClosedExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.EditExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.FinishedExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.ProgressExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.ReadyExamViewHolder
import com.bobrovskii.home.ui.examsAdapter.viewholders.TimesetExamViewHolder

class ExamsAdapter(
	private val onItemClicked: (Int) -> Unit,
	private val onDeleteClicked: (Int) -> Unit,
	private val onRestoreState: (Int) -> Unit,
) : RecyclerView.Adapter<BaseExamViewHolder>() {

	var exams: List<Exam>? = null
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun getItemViewType(position: Int) = when (exams?.get(position)?.state) {
		ExamStates.REDACTION -> R.layout.item_exam_edit

		ExamStates.TIME_SET  -> R.layout.item_timeset_exam

		ExamStates.READY     -> R.layout.item_exam_ready

		ExamStates.PROGRESS  -> R.layout.item_exam_progress

		ExamStates.FINISHED  -> R.layout.item_exam_finished

		ExamStates.CLOSED    -> R.layout.item_exam_closed

		else                 -> throw IllegalStateException("Unknown view")
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseExamViewHolder =
		when (viewType) {
			R.layout.item_exam_edit     -> EditExamViewHolder.from(parent)

			R.layout.item_timeset_exam  -> TimesetExamViewHolder.from(parent)

			R.layout.item_exam_ready    -> ReadyExamViewHolder.from(parent)

			R.layout.item_exam_progress -> ProgressExamViewHolder.from(parent)

			R.layout.item_exam_finished -> FinishedExamViewHolder.from(parent)

			R.layout.item_exam_closed   -> ClosedExamViewHolder.from(parent)

			else                        -> throw IllegalArgumentException("Invalid view type")
		}

	override fun onBindViewHolder(holder: BaseExamViewHolder, position: Int) {
		exams?.let {
			val item = it[position]
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
						onRestoreState = onRestoreState,
					)
				}

				is ReadyExamViewHolder    -> {
					holder.bind(
						item = item,
						onItemClicked = onItemClicked,
						onDeleteClicked = onDeleteClicked,
					)
				}

				is ProgressExamViewHolder -> {
					holder.bind(item = item) { onItemClicked(item.id) }
				}

				is FinishedExamViewHolder -> {
					holder.bind(
						item = item,
						onItemClicked = onItemClicked,
						onRestoreState = onRestoreState,
					)
				}

				is ClosedExamViewHolder   -> {
					holder.bind(item = item) { onItemClicked(item.id) }
				}

				else                      -> throw IllegalArgumentException("Invalid holder")
			}
		}
	}

	override fun getItemCount(): Int = exams?.size ?: 0
}