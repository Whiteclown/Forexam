package com.bobrovskii.exam.data.repository

import com.bobrovskii.exam.data.api.ExamApi
import com.bobrovskii.exam.data.dto.RequestExam
import com.bobrovskii.exam.data.mapper.toDto
import com.bobrovskii.exam.data.mapper.toEntity
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.entity.Period
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

	override suspend fun postExam(discipline: Discipline, examRule: ExamRule, groups: List<Group>, startTime: String) {
		api.postExam(
			RequestExam(
				discipline = discipline.toDto(),
				examRule = examRule.toDto(),
				groups = groups.map { it.toDto() },
				startTime = startTime
			)
		)
	}
}