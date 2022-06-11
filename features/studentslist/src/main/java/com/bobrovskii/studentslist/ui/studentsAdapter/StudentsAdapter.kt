package com.bobrovskii.studentslist.ui.studentsAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.studentslist.ui.studentsAdapter.viewholder.BaseStudentViewHolder
import com.bobrovskii.studentslist.ui.studentsAdapter.viewholder.StudentViewHolder

class StudentsAdapter(
	private val onItemClicked: (String) -> Unit,
) : RecyclerView.Adapter<BaseStudentViewHolder>() {

	var students: List<String>? = null
		set(value) {
			field = value
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseStudentViewHolder =
		StudentViewHolder.from(parent)

	override fun onBindViewHolder(holder: BaseStudentViewHolder, position: Int) {
		students?.let {
			val item = it[position]
			(holder as StudentViewHolder).bind(
				item = item,
				onItemClicked = onItemClicked,
			)
		}
	}

	override fun getItemCount(): Int = students?.size ?: 0
}