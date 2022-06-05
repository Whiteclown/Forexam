package com.bobrovskii.artefact.data.datasource

import com.bobrovskii.artefact.data.api.ArtefactApi
import com.bobrovskii.artefact.data.dto.ArtefactDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

interface ArtefactDataSource {

	suspend fun set(fileName: String, content: ByteArray): ArtefactDto

	suspend fun get(artefactId: Int): ResponseBody

	suspend fun getMetaData(artefactId: Int): ArtefactDto
}

class ArtefactDataSourceImpl @Inject constructor(
	private val api: ArtefactApi
) : ArtefactDataSource {

	private companion object {

		const val ARTEFACT_PART_NAME = "file"
		const val ARTEFACT_MEDIA_TYPE = "multipart/form-data"
	}

	override suspend fun set(fileName: String, content: ByteArray): ArtefactDto {
		val requestFile = RequestBody.create(ARTEFACT_MEDIA_TYPE.toMediaTypeOrNull(), content)
		val body = MultipartBody.Part.createFormData(ARTEFACT_PART_NAME, fileName, requestFile)

		return api.upload(body)
	}

	override suspend fun get(artefactId: Int): ResponseBody =
		api.download(artefactId)

	override suspend fun getMetaData(artefactId: Int): ArtefactDto =
		api.getMetaData(artefactId)
}