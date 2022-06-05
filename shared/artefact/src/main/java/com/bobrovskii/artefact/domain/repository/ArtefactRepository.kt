package com.bobrovskii.artefact.domain.repository

import android.net.Uri
import com.bobrovskii.artefact.domain.entity.ArtefactMetaData

interface ArtefactRepository {

	suspend fun getMetaData(artefactId: Int): ArtefactMetaData

	suspend fun upload(fileName: String, content: ByteArray): ArtefactMetaData

	suspend fun download(artefact: ArtefactMetaData): Uri
}