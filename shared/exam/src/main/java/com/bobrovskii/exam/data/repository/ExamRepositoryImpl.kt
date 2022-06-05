package com.bobrovskii.exam.data.repository

import android.content.Context
import com.bobrovskii.core.AnswerStates
import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.data.api.ExamApi
import com.bobrovskii.exam.data.dto.RequestAddExam
import com.bobrovskii.exam.data.dto.RequestAnswerRating
import com.bobrovskii.exam.data.dto.RequestExamState
import com.bobrovskii.exam.data.dto.RequestMessage
import com.bobrovskii.exam.data.dto.RequestUpdateExam
import com.bobrovskii.exam.data.dto.Token
import com.bobrovskii.exam.data.mapper.toEntity
import com.bobrovskii.exam.domain.entity.Account
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.exam.domain.entity.AnswerInfo
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.repository.ExamRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ExamRepositoryImpl @Inject constructor(
	private val api: ExamApi,
	@ApplicationContext private val context: Context,
) : ExamRepository {

	override suspend fun postExam(name: String, discipline: Discipline, groupId: Int, oneGroup: Boolean) =
		api.postExam(
			RequestAddExam(
				name = name,
				disciplineId = discipline.id,
				groupId = groupId,
				oneGroup = oneGroup,
			)
		)

	override suspend fun postMessage(answerId: Int, text: String, artefactId: Int?) =
		api.postMessage(answerId, RequestMessage(text, artefactId))

	override suspend fun sendFirebaseToken() {
		val token = context.getSharedPreferences("firebaseTokenPrefs", Context.MODE_PRIVATE)
			.getString("firebaseToken", null)
		token?.let {
			api.sendFirebaseToken(Token(it))
		}
	}

	override suspend fun saveFirebaseToken(token: String) {
		context.getSharedPreferences("firebaseTokenPrefs", Context.MODE_PRIVATE)
			.edit()
			.putString("firebaseToken", token)
			.apply()
	}

	override suspend fun getAccountById(accountId: Int): Account =
		api.getAccountById(accountId).toEntity()

	override suspend fun getExams(): List<Exam> =
		api.getExams().map { it.toEntity() }

	override suspend fun getExamById(examId: Int): Exam =
		api.getExamById(examId).toEntity()

	override suspend fun getDisciplines(): List<Discipline> =
		api.getDisciplines().map { it.toEntity() }

	override suspend fun getDisciplineById(disciplineId: Int): Discipline =
		api.getDisciplineById(disciplineId).toEntity()

	override suspend fun getGroups(): List<Group> =
		api.getGroups().map { it.toEntity() }

	override suspend fun getGroupById(groupId: Int) =
		api.getGroupById(groupId).toEntity()

	override suspend fun getAnswersByExam(examId: Int): List<Answer> {
		val answers: MutableList<Answer> = mutableListOf()
		api.getFullExamById(examId, 5).group.groupRatings.forEach { fullGroupRatingDto ->
			fullGroupRatingDto.studentRatings.forEach { fullStudentsRatingDto ->
				val acc = fullStudentsRatingDto.student.student.account
				fullStudentsRatingDto.answers.forEach {
					answers.add(it.answer.toEntity().apply {
						studentName = "${acc.surname} ${acc.name}"
						type = it.task.task.taskType
					})
				}
				fullStudentsRatingDto.student.student.account
			}
		}
		return answers
	}

	override suspend fun getAnswerInfo(answerId: Int): AnswerInfo =
		api.getFullAnswerById(answerId, 2).toEntity()

	override suspend fun updateExam(examId: Int, name: String, discipline: Discipline, groupId: Int, oneGroup: Boolean) =
		api.updateExam(
			RequestUpdateExam(
				id = examId,
				name = name,
				disciplineId = discipline.id,
				groupId = groupId,
				oneGroup = oneGroup,
			)
		)

	override suspend fun updateExamState(examId: Int, state: ExamStates, startTime: String?) =
		api.updateExamState(
			state = RequestExamState(examId, startTime, state)
		)

	override suspend fun updateAnswerRating(answerId: Int, state: AnswerStates, rating: Int?) =
		api.updateAnswerRating(RequestAnswerRating(answerId, state, rating))

	override suspend fun deleteExamById(examId: Int) =
		api.deleteExamById(examId)
}