package com.bobrovskii.exam.domain.repository

import com.bobrovskii.core.AnswerStates
import com.bobrovskii.core.ExamStates
import com.bobrovskii.exam.domain.entity.Account
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.exam.domain.entity.AnswerInfo
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.Group

interface ExamRepository {

	suspend fun postExam(name: String, discipline: Discipline, groupId: Int, oneGroup: Boolean)

	suspend fun postMessage(answerId: Int, text: String, artefactId: Int?)

	suspend fun sendFirebaseToken()

	suspend fun saveFirebaseToken(token: String)

	suspend fun getAccountById(accountId: Int): Account

	suspend fun getExams(): List<Exam>

	suspend fun getExamById(examId: Int): Exam

	suspend fun getDisciplines(): List<Discipline>

	suspend fun getDisciplineById(disciplineId: Int): Discipline

	suspend fun getGroups(): List<Group>

	suspend fun getGroupById(groupId: Int): Group

	suspend fun getAnswersByExam(examId: Int): List<Answer>

	suspend fun getAnswerInfo(answerId: Int): AnswerInfo

	suspend fun updateExam(examId: Int, name: String, discipline: Discipline, groupId: Int, oneGroup: Boolean)

	suspend fun updateExamState(examId: Int, state: ExamStates, startTime: String?)

	suspend fun updateAnswerRating(answerId: Int, state: AnswerStates, rating: Int?)

	suspend fun deleteExamById(examId: Int)
}