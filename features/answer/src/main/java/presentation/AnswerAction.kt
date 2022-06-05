package presentation

sealed interface AnswerAction {
	data class ShowError(val message: String) : AnswerAction
}