package com.bobrovskii.studentslist.ui.answersAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.core.AnswerStates
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.studentslist.R
import com.bobrovskii.studentslist.ui.answersAdapter.viewholder.BaseAnswerViewHolder
import com.bobrovskii.studentslist.ui.answersAdapter.viewholder.CheckingAnswerViewHolder
import com.bobrovskii.studentslist.ui.answersAdapter.viewholder.InProgressAnswerViewHolder
import com.bobrovskii.studentslist.ui.answersAdapter.viewholder.NoRatingAnswerViewHolder
import com.bobrovskii.studentslist.ui.answersAdapter.viewholder.RatedAnswerViewHolder
import com.bobrovskii.studentslist.ui.answersAdapter.viewholder.SentAnswerViewHolder

class AnswersAdapter(
	private val onItemClicked: (Int) -> Unit,
) : RecyclerView.Adapter<BaseAnswerViewHolder>() {

	var answers: List<Answer>? = null
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun getItemViewType(position: Int): Int = when (answers?.get(position)?.state) {
		AnswerStates.CHECKING    -> R.layout.item_checking_answer

		AnswerStates.SENT        -> R.layout.item_sent_answer

		AnswerStates.RATED       -> R.layout.item_rated_answer

		AnswerStates.IN_PROGRESS -> R.layout.item_in_progress_answer

		AnswerStates.NO_RATING   -> R.layout.item_no_rating_answer

		else                     -> throw Exception("Unknown view")
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAnswerViewHolder =
		when (viewType) {
			R.layout.item_checking_answer    -> CheckingAnswerViewHolder.from(parent)

			R.layout.item_sent_answer        -> SentAnswerViewHolder.from(parent)

			R.layout.item_rated_answer       -> RatedAnswerViewHolder.from(parent)

			R.layout.item_in_progress_answer -> InProgressAnswerViewHolder.from(parent)

			R.layout.item_no_rating_answer   -> NoRatingAnswerViewHolder.from(parent)

			else                             -> throw Exception("Invalid view type")
		}

	override fun onBindViewHolder(holder: BaseAnswerViewHolder, position: Int) {
		answers?.let {
			val item = it[position]
			when (holder) {
				is CheckingAnswerViewHolder   -> {
					holder.bind(
						item = item,
						onItemClicked = onItemClicked,
					)
				}

				is SentAnswerViewHolder       -> {
					holder.bind(
						item = item,
						onItemClicked = onItemClicked,
					)
				}

				is InProgressAnswerViewHolder -> {
					holder.bind(
						item = item,
						onItemClicked = onItemClicked,
					)
				}

				is RatedAnswerViewHolder      -> {
					holder.bind(
						item = item,
						onItemClicked = onItemClicked,
					)
				}
			}
		}
	}

	override fun getItemCount(): Int = answers?.size ?: 0
}