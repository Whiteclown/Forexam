package com.bobrovskii.core

import android.util.Base64

object Base64Encoder {

	private const val FORMAT = "%s:%s"

	fun encode(firstValue: String, secondValue: String): String {
		val preparedData = prepareData(firstValue = firstValue, secondValue = secondValue)
		return Base64.encodeToString(preparedData.toByteArray(), Base64.NO_WRAP)
	}

	private fun prepareData(firstValue: String, secondValue: String) =
		String.format(FORMAT, firstValue, secondValue)
}