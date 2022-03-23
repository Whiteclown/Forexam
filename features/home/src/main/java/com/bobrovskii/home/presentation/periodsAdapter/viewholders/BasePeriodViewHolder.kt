package com.bobrovskii.home.presentation.periodsAdapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bobrovskii.exam.domain.entity.Period

abstract class BasePeriodViewHolder(DB: View): RecyclerView.ViewHolder(DB) {
	protected lateinit var period: Period
	fun getPeriodItem() = period
}