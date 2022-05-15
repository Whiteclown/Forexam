package com.bobrovskii.accessexamination.ui.adapter.viewholders

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.accessexamination.databinding.ItemStudentBinding
import com.bobrovskii.exam.domain.entity.Ticket

class TicketViewHolder(
	private val binding: ItemStudentBinding,
) :
	RecyclerView.ViewHolder(binding.root) {

	fun bind(
		item: Ticket,
		onSemesterRatingChanged: (Int, Int) -> Unit,
		onAllowanceChanged: (Int, Boolean) -> Unit,
	) {
		with(binding) {
			//Set data and listeners
			etSemesterRating.setText(item.semesterRating.toString())
			cbAllowance.isChecked = item.allowed
			student.text = item.studentName

			etSemesterRating.addTextChangedListener(object: TextWatcher{
				override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

				override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
					try {
						onSemesterRatingChanged(item.id, p0.toString().toInt())
					} catch (e: Exception) {

					}
				}

				override fun afterTextChanged(p0: Editable?) = Unit
			})

			cbAllowance.setOnCheckedChangeListener { compoundButton, isChecked ->
				onAllowanceChanged(item.id, isChecked)
			}
		}
	}

	companion object {

		fun from(parent: ViewGroup): TicketViewHolder {
			val layoutInflater = LayoutInflater.from(parent.context)
			val binding = ItemStudentBinding.inflate(layoutInflater, parent, false)
			return TicketViewHolder(binding)
		}
	}
}