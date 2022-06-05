package com.bobrovskii.artefact.data.api

import com.bobrovskii.artefact.data.dto.ArtefactDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming

interface ArtefactApi {

	@POST("/artefact/upload")
	@Multipart
	suspend fun upload(@Part part: MultipartBody.Part): ArtefactDto

	@GET("/artefact/{artefactId}/download")
	@Streaming
	suspend fun download(@Path("artefactId") artefactId: Int): ResponseBody

	@GET("/artefact/{artefactId}/info")
	suspend fun getMetaData(@Path("artefactId") artefactId: Int): ArtefactDto
}