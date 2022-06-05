package com.bobrovskii.artefact.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtefactDto(
	val id: Int,
	val fileSize: Int,
	val artefactType: String,
	val fileName: String
)