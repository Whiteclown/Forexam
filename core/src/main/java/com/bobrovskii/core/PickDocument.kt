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

/*
fun Fragment.saveAndOpenFile(fileName: String, byteArray: ByteArray) {

	val contentResolver = context!!.contentResolver

	var values = ContentValues()
	values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
	values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
	values.put(MediaStore.MediaColumns.IS_PENDING, 1)

	val mediaUri = contentResolver.insert(
		MediaStore.Downloads.EXTERNAL_CONTENT_URI,
		values
	)

	contentResolver.openOutputStream(mediaUri!!).use { out ->
		// Write your data here
		out?.write(byteArray)
	}

	values = ContentValues()
	values.put(MediaStore.MediaColumns.IS_PENDING, 0)

	contentResolver.update(mediaUri, values, null, null)

	val uri = try {
		requireActivity().openFileOutput(fileName, MODE_PRIVATE).use {
			it.write(byteArray)
		}
		//FileProvider.getUriForFile(requireActivity(), (context?.applicationContext?.packageName ?: throw FileNotFoundException()) + ".provider", requireActivity().getFileStreamPath(fileName))
		Uri.fromFile(requireActivity().getFileStreamPath(fileName))
	} catch (exception: FileNotFoundException) {
		throw IOException()
	}
	val mime: String? = requireActivity().contentResolver.getType(uri)

	// Open file with user selected app
	Intent(Intent.ACTION_VIEW).apply {
		setDataAndType(uri, mime)
		flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
		startActivity(this)
	}
}*/
