package com.bobrovskii.exam.data.mapper

import com.bobrovskii.exam.data.dto.DisciplineDto
import com.bobrovskii.exam.data.dto.ExamDto
import com.bobrovskii.exam.data.dto.ExamRuleDto
import com.bobrovskii.exam.data.dto.GroupDto
import com.bobrovskii.exam.data.dto.PeriodDto
import com.bobrovskii.exam.data.dto.StudentDto
import com.bobrovskii.exam.data.dto.TicketDto
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.entity.Period
import com.bobrovskii.exam.domain.entity.Student
import com.bobrovskii.exam.domain.entity.Ticket

fun ExamDto.toEntity() =
	Exam(
		id = id,
		examRuleId = examRuleId,
		disciplineId = disciplineId,
		groupIds = groupIds,
	)

fun PeriodDto.toEntity() =
	Period(
		id = id,
		start = start,
		end = end,
		examId = examId,
		state = state,
		discipline = null,
	)

fun DisciplineDto.toEntity() =
	Discipline(
		id = id,
		name = name
	)

fun Discipline.toDto() =
	DisciplineDto(
		id = id,
		name = name
	)

fun GroupDto.toEntity() =
	Group(
		id = id,
		name = name
	)

fun Group.toDto() =
	GroupDto(
		id = id,
		name = name
	)

fun ExamRuleDto.toEntity() =
	ExamRule(
		id = id,
		name = name,
		questionCount = questionCount,
		exerciseCount = exerciseCount,
		duration = duration,
		minimalRating = minimalRating,
		themeIds = themeIds,
		disciplineId = disciplineId,
	)

fun ExamRule.toDto() =
	ExamRuleDto(
		id = id,
		name = name,
		questionCount = questionCount,
		exerciseCount = exerciseCount,
		duration = duration,
		minimalRating = minimalRating,
		themeIds = themeIds,
		disciplineId = disciplineId,
	)

fun TicketDto.toEntity() =
	Ticket(
		id = id,
		semesterRating = semesterRating,
		examRating = examRating,
		allowed = allowed,
		examPeriodId = examPeriodId,
		studentId = studentId,
		studentName = null,
	)

fun StudentDto.toEntity() =
	Student(
		name = account.name,
		surname = account.surname,
	)