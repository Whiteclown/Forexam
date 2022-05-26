package com.bobrovskii.exam.data.api

import com.bobrovskii.exam.data.dto.DisciplineDto
import com.bobrovskii.exam.data.dto.ExamDto
import com.bobrovskii.exam.data.dto.GroupDto
import com.bobrovskii.exam.data.dto.RequestAddExam
import com.bobrovskii.exam.data.dto.RequestExamState
import com.bobrovskii.exam.data.dto.RequestUpdateExam
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExamApi {

	@POST("/exams")
	suspend fun postExam(@Body requestAddExam: RequestAddExam)

	@GET("/exams")
	suspend fun getExams(): List<ExamDto>

	@GET("/exams/{examId}")
	suspend fun getExamById(@Path("examId") examId: Int): ExamDto

	@GET("/discipline")
	suspend fun getDisciplines(): List<DisciplineDto>

	@GET("/discipline/{disciplineId}")
	suspend fun getDisciplineById(@Path("disciplineId") disciplineId: Int): DisciplineDto

	@GET("/groups")
	suspend fun getGroups(): List<GroupDto>

	@GET("/groups/{groupId}")
	suspend fun getGroupById(@Path("groupId") groupId: Int): GroupDto

	@PUT("/exams")
	suspend fun updateExam(@Body requestUpdateExam: RequestUpdateExam)

	@PUT("/exams/state")
	suspend fun updateExamState(@Body state: RequestExamState)

	@DELETE("/exams/{examId}")
	suspend fun deleteExamById(@Path("examId") examId: Int)
}