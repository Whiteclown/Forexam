package com.bobrovskii.exam.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FullExamDto(
	val exam: ExamDto,
	val group: FullGroupDto,
)

@JsonClass(generateAdapter = true)
data class FullGroupDto(
	val groupRatings: List<FullGroupRatingDto>,
)

@JsonClass(generateAdapter = true)
data class FullGroupRatingDto(
	val studentRatings: List<FullStudentRatingDto>,
)

@JsonClass(generateAdapter = true)
data class FullStudentRatingDto(
	val answers: List<FullAnswerDto>,
	val student: FullStudentDto,
)

@JsonClass(generateAdapter = true)
data class FullStudentDto(
	val student: StudentDto,
)

@JsonClass(generateAdapter = true)
data class FullAnswerDto(
	val answer: AnswerDto,
	val task: FullTaskDto,
)

@JsonClass(generateAdapter = true)
data class FullTaskDto(
	val task: TaskDto,
)

@JsonClass(generateAdapter = true)
data class StudentDto(
	val account: AccountDto,
)

@JsonClass(generateAdapter = true)
data class AccountDto(
	val id: Int,
	val name: String,
	val surname: String,
)