package presentation

import android.net.Uri
import com.bobrovskii.artefact.domain.entity.ArtefactMetaData
import com.bobrovskii.exam.domain.entity.AnswerInfo

sealed interface AnswerState {
	object Initial : AnswerState

	object Loading : AnswerState

	data class Content(
		val answerInfo: AnswerInfo,
		val metaData: ArtefactMetaData?,
		val file: Uri?,
	) : AnswerState
}