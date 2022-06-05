package com.bobrovskii.artefact.data.repository

import android.net.Uri
import com.bobrovskii.artefact.data.datasource.ArtefactDataSource
import com.bobrovskii.artefact.data.datasource.DocumentDataSource
import com.bobrovskii.artefact.data.mapper.toEntity
import com.bobrovskii.artefact.domain.entity.ArtefactMetaData
import com.bobrovskii.artefact.domain.repository.ArtefactRepository
import javax.inject.Inject

class ArtefactRepositoryImpl @Inject constructor(
	private val documentDataSource: DocumentDataSource,
	private val artefactDataSource: ArtefactDataSource,
) : ArtefactRepository {

	override suspend fun getMetaData(artefactId: Int): ArtefactMetaData =
		artefactDataSource.getMetaData(artefactId).toEntity()

	override suspend fun upload(fileName: String, content: ByteArray): ArtefactMetaData =
		artefactDataSource.set(fileName, content).toEntity()

	override suspend fun download(artefact: ArtefactMetaData): Uri =
		getLocal(artefact) ?: getRemote(artefact)

	private suspend fun getLocal(artefact: ArtefactMetaData): Uri? = documentDataSource.get(artefact)

	private suspend fun getRemote(artefact: ArtefactMetaData): Uri {
		val responseBody = run { artefactDataSource.get(artefact.id) }

		val uri = documentDataSource.set(artefact, responseBody)

		return uri ?: throw Exception("Can't load file")
	}
}