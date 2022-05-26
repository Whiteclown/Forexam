package com.bobrovskii.exam.data.mapper

import com.bobrovskii.exam.data.dto.DisciplineDto
import com.bobrovskii.exam.data.dto.ExamDto
import com.bobrovskii.exam.data.dto.GroupDto
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.Group

fun ExamDto.toEntity() =
	Exam(
		id = id,
		name = name,
		disciplineId = disciplineId,
		groupId = groupId,
		oneGroup = oneGroup,
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