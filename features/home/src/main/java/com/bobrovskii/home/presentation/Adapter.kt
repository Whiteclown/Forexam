package com.bobrovskii.home.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.home.R

class Adapter(private val names: List<Name>) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

	class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
		val title: TextView = itemView.findViewById(R.id.textViewTitle)
		val description: TextView = itemView.findViewById(R.id.textViewDescription)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
		val itemView = LayoutInflater.from(parent.context)
			.inflate(R.layout.exam_item, parent, false)
		return MyViewHolder(itemView)
	}

	override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
		holder.title.text = names[position].title
		holder.description.text = names[position].description
	}

	override fun getItemCount(): Int = names.size
}