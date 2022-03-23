package com.bobrovskii.exam.domain.repository

import com.bobrovskii.exam.data.dto.DisciplineDto
import com.bobrovskii.exam.data.dto.ExamRuleDto
import com.bobrovskii.exam.data.dto.GroupDto
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.entity.Period
import retrofit2.http.Body

interface ExamRepository {

	suspend fun getExams(): List<Exam>

	suspend fun getPeriods(examId: Int): List<Period>

	suspend fun getDisciplines(): List<Discipline>

	suspend fun getExamRulesByDiscipline(disciplineId: Int): List<ExamRule>

	suspend fun getGroupsByDiscipline(disciplineId: Int): List<Group>

	suspend fun postExam(discipline: Discipline, examRule: ExamRule, groups: List<Group>, startTime: String)
}