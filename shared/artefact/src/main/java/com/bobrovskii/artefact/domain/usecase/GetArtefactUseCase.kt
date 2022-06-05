package com.bobrovskii.artefact.domain.usecase

import android.net.Uri
import com.bobrovskii.artefact.domain.entity.ArtefactMetaData
import com.bobrovskii.artefact.domain.repository.ArtefactRepository
import javax.inject.Inject

class GetArtefactUseCase @Inject constructor(
	private val repository: ArtefactRepository
) {

	suspend operator fun invoke(artefact: ArtefactMetaData): Uri =
		repository.download(artefact)
}