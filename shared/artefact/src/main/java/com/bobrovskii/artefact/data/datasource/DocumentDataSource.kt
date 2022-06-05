package com.bobrovskii.artefact.data.datasource

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.bobrovskii.artefact.domain.entity.ArtefactMetaData
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface DocumentDataSource {

	suspend fun set(artefact: ArtefactMetaData, responseBody: ResponseBody): Uri?

	suspend fun get(artefact: ArtefactMetaData): Uri?
}

class DocumentDataSourceImpl @Inject constructor(
	@ApplicationContext private val context: Context,
) : DocumentDataSource {

	override suspend fun set(artefact: ArtefactMetaData, responseBody: ResponseBody): Uri? =
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			// version >= 29 (Android 10, 11, ...)
			val contentValues = ContentValues().apply {
				put(MediaStore.MediaColumns.DISPLAY_NAME, artefact.fullName)
			}
			val byteArray = responseBody.byteStream().use {
				it.readBytes()
			}
			val dstUri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
			if (dstUri != null) {
				context.contentResolver.openOutputStream(dstUri).use {
					it?.write(byteArray)
				}
				dstUri
			} else {
				null
			}
		} else {
			// version < 29 (Android ..., 7,8,9)
			val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
			val byteArray = responseBody.byteStream().use {
				it.readBytes()
			}
			if (downloadDir.canWrite()) {
				val file = File(downloadDir, artefact.fullName)
				FileOutputStream(file).use {
					it.write(byteArray)
				}
				FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
			} else {
				null
			}
		}

	override suspend fun get(artefact: ArtefactMetaData): Uri? =
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			// version >= 29 (Android 10, 11, ...)
			null
		} else {
			// version < 29 (Android ..., 7,8,9)
			val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
			val file = downloadDir.listFiles()?.find { it.name.equals(artefact.fullName) }
			file?.let { FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", it) }
		}
}