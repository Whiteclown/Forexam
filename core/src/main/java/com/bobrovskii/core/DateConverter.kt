package com.bobrovskii.core

import java.text.SimpleDateFormat
import java.util.Date

fun String.toDate(): String {
	val date = Date(this.toLong())
	val df = SimpleDateFormat("y-MM-dd HH:mm")
	return df.format(date)
}

fun String.toTimestamp(): String {
	val df = SimpleDateFormat("y-MM-dd HH:mm")
	val date = df.parse(this)
	return date.time.toString()
}