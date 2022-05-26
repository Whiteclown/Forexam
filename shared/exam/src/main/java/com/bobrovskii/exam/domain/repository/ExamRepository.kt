package com.bobrovskii.exam.domain.repository

import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.Group

interface ExamRepository {

	suspend fun postExam(name: String, discipline: Discipline, groupId: Int, oneGroup: Boolean)

	suspend fun getExams(): List<Exam>

	suspend fun getExamById(examId: Int): Exam

	suspend fun getDisciplines(): List<Discipline>

	suspend fun getDisciplineById(disciplineId: Int): Discipline

	suspend fun getGroups(): List<Group>

	suspend fun getGroupById(groupId: Int): Group

	suspend fun updateExam(examId: Int, name: String, discipline: Discipline, groupId: Int, oneGroup: Boolean)

	suspend fun updateExamState(examId: Int, state: String, startTime: String?)

	suspend fun deleteExamById(examId: Int)
}