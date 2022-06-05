package com.bobrovskii.artefact.domain.usecase

import com.bobrovskii.artefact.domain.repository.ArtefactRepository
import javax.inject.Inject

class PostArtefactUseCase @Inject constructor(
	private val repository: ArtefactRepository,
) {

	suspend operator fun invoke(fileName: String, content: ByteArray) =
		repository.upload(fileName, content)
}