package com.bobrovskii.core

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import android.view.View
import android.widget.Button
import java.util.Calendar

fun ShowDateAndTimePicker(context: Context, view: View?) {
	var calendar: Calendar = Calendar.getInstance()
	val day = calendar.get(Calendar.DAY_OF_MONTH)
	val month = calendar.get(Calendar.MONTH)
	val year = calendar.get(Calendar.YEAR)
	DatePickerDialog(
		context,
		DatePickerDialog.OnDateSetListener { _, yearPicked, monthPicked, dayPicked ->
			calendar = Calendar.getInstance()
			val hour = calendar.get(Calendar.HOUR)
			val minute = calendar.get(Calendar.MINUTE)
			TimePickerDialog(
				context,
				TimePickerDialog.OnTimeSetListener { _, hourPicked, minutePicked ->
					val monthNormal = monthPicked + 1
					val monthStr = if (monthNormal < 10) "0$monthNormal" else monthNormal.toString()
					val dayStr = if (dayPicked < 10) "0$dayPicked" else dayPicked.toString()
					val hourStr = if (hourPicked < 10) "0$hourPicked" else hourPicked.toString()
					val minuteStr = if (minutePicked < 10) "0$minutePicked" else minutePicked.toString()
					(view as Button).text = context.getString(R.string.date_picker_text, yearPicked, monthStr, dayStr, hourStr, minuteStr)
				},
				hour,
				minute,
				DateFormat.is24HourFormat(context)
			).show()
		},
		year,
		month,
		day
	).show()
}