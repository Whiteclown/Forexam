package com.bobrovskii.exam.data.mapper

import com.bobrovskii.exam.data.dto.DisciplineDto
import com.bobrovskii.exam.data.dto.ExamDto
import com.bobrovskii.exam.data.dto.ExamRuleDto
import com.bobrovskii.exam.data.dto.GroupDto
import com.bobrovskii.exam.data.dto.PeriodDto
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.ExamRule
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.entity.Period

fun ExamDto.toEntity() =
	Exam(
			id = id,
			name = examRule.discipline.name,
		//startTime = startTime
	)

fun PeriodDto.toEntity() =
	Period(
		id = id,
		start = start,
		end = end,
		state = state
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
		minimalRating = minimalRating
	)

fun ExamRule.toDto() =
	ExamRuleDto(
		id = id,
		name = name,
		questionCount = questionCount,
		exerciseCount = exerciseCount,
		duration = duration,
		minimalRating = minimalRating,
		themes = null,
		discipline = DisciplineDto(-1, "")
	)