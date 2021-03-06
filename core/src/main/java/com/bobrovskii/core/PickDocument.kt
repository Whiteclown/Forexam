package com.bobrovskii.core

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import java.io.FileNotFoundException

const val OPEN_DOCUMENT_REQUEST_CODE = 2

fun Fragment.tryHandleOpenDocumentResult(requestCode: Int, resultCode: Int, data: Intent?): OpenFileResult {
	return if (requestCode == OPEN_DOCUMENT_REQUEST_CODE) {
		handleOpenDocumentResult(resultCode, data)
	} else OpenFileResult.DifferentResult
}

private fun Fragment.handleOpenDocumentResult(resultCode: Int, data: Intent?): OpenFileResult {
	return if (resultCode == Activity.RESULT_OK && data != null) {
		val contentUri = data.data
		if (contentUri != null) {
			val stream =
				try {
					requireActivity().application.contentResolver.openInputStream(contentUri)
				} catch (exception: FileNotFoundException) {
					return OpenFileResult.ErrorOpeningFile
				}

			val fileName = requireContext().contentResolver.queryFileName(contentUri)

			if (stream != null && fileName != null) {
				val content = stream.readBytes()
				stream.close()

				OpenFileResult.FileWasOpened(fileName, content)
			} else OpenFileResult.ErrorOpeningFile
		} else {
			OpenFileResult.ErrorOpeningFile
		}
	} else {
		OpenFileResult.OpenFileWasCancelled
	}
}

sealed class OpenFileResult {
	object OpenFileWasCancelled : OpenFileResult()
	data class FileWasOpened(val fileName: String, val content: ByteArray) : OpenFileResult()
	object ErrorOpeningFile : OpenFileResult()
	object DifferentResult : OpenFileResult()
}