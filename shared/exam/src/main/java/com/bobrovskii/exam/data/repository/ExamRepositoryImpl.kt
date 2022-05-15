package com.bobrovskii.exam.data.repository

import com.bobrovskii.exam.data.api.ExamApi
import com.bobrovskii.exam.data.dto.RequestExam
import com.bobrovskii.exam.data.dto.RequestPeriodState
import com.bobrovskii.exam.data.dto.RequestTicketsRating
import com.bobrovskii.exam.data.mapper.toEntity
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.exam.domain.entity.Student
import com.bobrovskii.exam.domain.entity.Ticket
import com.bobrovskii.exam.domain.repository.ExamRepository
import javax.inject.Inject

class ExamRepositoryImpl @Inject constructor(
	private val api: ExamApi
) : ExamRepository {

	override suspend fun getExams(): List<Exam> =
		api.getExams().map { it.toEntity() }

	override suspend fun getPeriods(examId: Int): List<Period> =
		api.getPeriodsByExam(examId).map { it.toEntity() }

	override suspend fun getDisciplines(): List<Discipline> =
		api.getDisciplines().map { it.toEntity() }

	override suspend fun getExamRulesByDiscipline(disciplineId: Int): List<ExamRule> =
		api.getExamRulesByDiscipline(disciplineId).map { it.toEntity() }

	override suspend fun getGroupsByDiscipline(disciplineId: Int): List<Group> =
		api.getGroupsByDiscipline(disciplineId).map { it.toEntity() }

	override suspend fun postExam(discipline: Discipline, examRule: ExamRule, groups: List<Group>, startTime: String) =
		api.postExam(
			RequestExam(
				examRuleId = examRule.id,
				disciplineId = discipline.id,
				groupIds = groups.map { it.id },
				startTime = startTime,
			)
		)

	override suspend fun getDisciplineById(disciplineId: Int): Discipline =
		api.getDisciplineById(disciplineId).toEntity()

	override suspend fun deleteExamById(examId: Int) =
		api.deleteExamById(examId)

	override suspend fun getExamById(examId: Int): Exam =
		api.getExamById(examId).toEntity()

	override suspend fun getExamRuleById(examRuleId: Int): ExamRule =
		api.getExamRuleById(examRuleId).toEntity()

	override suspend fun getLastPeriodByExam(examId: Int): Period =
		api.getLastPeriodByExam(examId).toEntity()

	override suspend fun updateExam(examId: Int, discipline: Discipline, examRule: ExamRule, groups: List<Group>, startTime: String) =
		api.updateExam(
			examId,
			RequestExam(
				examRuleId = examRule.id,
				disciplineId = discipline.id,
				groupIds = groups.map { it.id },
				startTime = startTime,
			)
		)

	override suspend fun updatePeriodState(periodId: Int, state: String) =
		api.updatePeriodState(
			periodId = periodId,
			state = RequestPeriodState(state)
		)

	override suspend fun getUnpassedTickets(examId: Int) =
		api.getUnpassedTickets(examId).map { it.toEntity() }

	override suspend fun updateTicketsRating(ticketsRating: List<Ticket>) {
		val requestTicketsRating: MutableList<RequestTicketsRating> = mutableListOf()
		ticketsRating.forEach {
			requestTicketsRating.add(RequestTicketsRating(it.id, it.semesterRating, it.examRating, it.allowed))
		}
		api.updateTicketsRating(
			requestTicketsRating
		)
	}

	override suspend fun getStudentById(studentId: Int): Student =
		api.getStudentById(studentId).toEntity()
}