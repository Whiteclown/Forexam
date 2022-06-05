package com.bobrovskii.artefact.domain.usecase

import com.bobrovskii.artefact.domain.repository.ArtefactRepository
import javax.inject.Inject

class GetArtefactMetaDataUseCase @Inject constructor(
	private val repository: ArtefactRepository,
) {

	suspend operator fun invoke(artefactId: Int) =
		repository.getMetaData(artefactId)
}