package com.bobrovskii.progressexamination.ui.answersAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.progressexamination.R
import com.bobrovskii.progressexamination.presentation.AnswersState
import com.bobrovskii.progressexamination.ui.answersAdapter.viewholder.BaseAnswerViewHolder
import com.bobrovskii.progressexamination.ui.answersAdapter.viewholder.CheckingAnswerViewHolder
import com.bobrovskii.progressexamination.ui.answersAdapter.viewholder.InProgressAnswerViewHolder
import com.bobrovskii.progressexamination.ui.answersAdapter.viewholder.NoRatingAnswerViewHolder
import com.bobrovskii.progressexamination.ui.answersAdapter.viewholder.RatedAnswerViewHolder
import com.bobrovskii.progressexamination.ui.answersAdapter.viewholder.SentAnswerViewHolder

class AnswersAdapter(
	private val onItemClicked: (Int) -> Unit,
) : RecyclerView.Adapter<BaseAnswerViewHolder>() {

	var answers: List<Answer>? = null
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun getItemViewType(position: Int): Int = when (answers?.get(position)?.state) {
		AnswersState.CHECKING    -> R.layout.item_checking_answer

		AnswersState.SENT        -> R.layout.item_sent_answer

		AnswersState.RATED       -> R.layout.item_rated_answer

		AnswersState.IN_PROGRESS -> R.layout.item_in_progress_answer

		AnswersState.NO_RATING   -> R.layout.item_no_rating_answer

		else                     -> throw Exception("ai ai ai")
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAnswerViewHolder =
		when (viewType) {
			R.layout.item_checking_answer    -> CheckingAnswerViewHolder.from(parent)

			R.layout.item_sent_answer        -> SentAnswerViewHolder.from(parent)

			R.layout.item_rated_answer       -> RatedAnswerViewHolder.from(parent)

			R.layout.item_in_progress_answer -> InProgressAnswerViewHolder.from(parent)

			R.layout.item_no_rating_answer   -> NoRatingAnswerViewHolder.from(parent)

			else                             -> throw Exception("yao error")
		}

	override fun onBindViewHolder(holder: BaseAnswerViewHolder, position: Int) {
		answers?.let {
			val item = it[position]
			when (holder) {
				is CheckingAnswerViewHolder -> {
					holder.bind(
						item = item,
					)
				}

				is SentAnswerViewHolder     -> {
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