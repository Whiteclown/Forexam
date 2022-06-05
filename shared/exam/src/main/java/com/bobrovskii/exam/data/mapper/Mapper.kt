package com.bobrovskii.exam.data.mapper

import com.bobrovskii.exam.data.dto.AccountDto
import com.bobrovskii.exam.data.dto.AnswerDto
import com.bobrovskii.exam.data.dto.AnswerInfoDto
import com.bobrovskii.exam.data.dto.ArtefactDto
import com.bobrovskii.exam.data.dto.DisciplineDto
import com.bobrovskii.exam.data.dto.ExamDto
import com.bobrovskii.exam.data.dto.GroupDto
import com.bobrovskii.exam.data.dto.TaskDto
import com.bobrovskii.exam.domain.entity.Account
import com.bobrovskii.exam.domain.entity.Answer
import com.bobrovskii.exam.domain.entity.AnswerInfo
import com.bobrovskii.exam.domain.entity.Artefact
import com.bobrovskii.exam.domain.entity.Discipline
import com.bobrovskii.exam.domain.entity.Exam
import com.bobrovskii.exam.domain.entity.Group
import com.bobrovskii.exam.domain.entity.Message
import com.bobrovskii.exam.domain.entity.Task

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

fun GroupDto.toEntity() =
	Group(
		id = id,
		name = name
	)

fun AnswerDto.toEntity() =
	Answer(
		id = id,
		taskId = taskId,
		rating = rating,
		studentRatingId = studentRatingId,
		number = number,
		state = state,
	)

fun TaskDto.toEntity() =
	Task(
		id = id,
		text = text,
		taskType = taskType,
	)

fun AnswerInfoDto.toEntity() =
	AnswerInfo(
		answer = answer.toEntity(),
		task = task.task.toEntity(),
		messages = messages.map {
			Message(
				id = it.message.id,
				text = it.message.text,
				sendTime = it.message.sendTime,
				senderName = "${it.account.surname} ${it.account.name}",
				artefact = it.artefact?.toEntity(),
				accountId = it.account.id
			)
		}
	)

fun ArtefactDto.toEntity() =
	Artefact(id, artefactType, fileName)

fun AccountDto.toEntity() =
	Account(
		name = name,
		surname = surname,
	)