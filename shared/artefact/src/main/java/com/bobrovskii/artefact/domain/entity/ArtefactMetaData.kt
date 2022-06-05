package com.bobrovskii.artefact.domain.entity

import android.webkit.MimeTypeMap
import kotlin.math.roundToInt

data class ArtefactMetaData(
	val id: Int,
	val size: Int,
	val extension: Extension,
	val fullName: String
) {

	enum class Extension {
		UNKNOWN,
		PNG,
		JPG,
		JPEG,
		TXT,
		DOC,
		DOCX,
		PDF,
		XML,
		ZIP,
		RAR;

		private companion object {

			val documentTypes = values().toList().minus(PNG).minus(JPG).minus(JPEG)
			const val UNKNOWN_MIME_TYPE = "*/*"
		}

		fun isDocument(): Boolean =
			this in documentTypes

		fun mimeType(): String =
			MimeTypeMap.getSingleton().getMimeTypeFromExtension(toString()) ?: UNKNOWN_MIME_TYPE
	}

	val sizeInMegaByte: Int
		get() = (size * 9.54E-7).roundToInt()

	val sizeInKiloByte: Int
		get() = (size * 9.77E-4).roundToInt()

	val mimeType: String
		get() = extension.mimeType()

	val isMedia: Boolean
		get() = extension.isDocument().not()

	val isDocument: Boolean
		get() = extension.isDocument()

	val savedName: String
		get() = "${id}_${fullName}"
}