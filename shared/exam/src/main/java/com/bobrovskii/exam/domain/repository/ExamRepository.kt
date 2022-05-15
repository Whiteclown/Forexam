package com.bobrovskii.exam.domain.repository

import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.exam.domain.entity.Student
import com.bobrovskii.exam.domain.entity.Ticket

interface ExamRepository {

	suspend fun getExams(): List<Exam>

	suspend fun getPeriods(examId: Int): List<Period>

	suspend fun getDisciplines(): List<Discipline>

	suspend fun getExamRulesByDiscipline(disciplineId: Int): List<ExamRule>

	suspend fun getGroupsByDiscipline(disciplineId: Int): List<Group>

	suspend fun postExam(discipline: Discipline, examRule: ExamRule, groups: List<Group>, startTime: String)

	suspend fun getDisciplineById(disciplineId: Int): Discipline

	suspend fun deleteExamById(examId: Int)

	suspend fun getExamById(examId: Int): Exam

	suspend fun getExamRuleById(examRuleId: Int): ExamRule

	suspend fun getLastPeriodByExam(examId: Int): Period

	suspend fun updateExam(examId: Int, discipline: Discipline, examRule: ExamRule, groups: List<Group>, startTime: String)

	suspend fun updatePeriodState(periodId: Int, state: String)

	suspend fun getUnpassedTickets(examId: Int): List<Ticket>

	suspend fun updateTicketsRating(ticketsRating: List<Ticket>)

	suspend fun getStudentById(studentId: Int): Student
}