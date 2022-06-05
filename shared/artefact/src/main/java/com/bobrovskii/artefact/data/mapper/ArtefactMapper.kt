package com.bobrovskii.artefact.data.mapper

import com.bobrovskii.artefact.data.dto.ArtefactDto
import com.bobrovskii.artefact.domain.entity.ArtefactMetaData

fun ArtefactDto.toEntity() =
	ArtefactMetaData(
		id = id,
		size = fileSize,
		extension = ArtefactMetaData.Extension.valueOf(artefactType),
		fullName = fileName
	)