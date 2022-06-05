package com.bobrovskii.exam.data.api

import com.bobrovskii.exam.data.dto.AccountDto
import com.bobrovskii.exam.data.dto.AnswerInfoDto
import com.bobrovskii.exam.data.dto.DisciplineDto
import com.bobrovskii.exam.data.dto.ExamDto
import com.bobrovskii.exam.data.dto.FullExamDto
import com.bobrovskii.exam.data.dto.GroupDto
import com.bobrovskii.exam.data.dto.RequestAddExam
import com.bobrovskii.exam.data.dto.RequestAnswerRating
import com.bobrovskii.exam.data.dto.RequestExamState
import com.bobrovskii.exam.data.dto.RequestMessage
import com.bobrovskii.exam.data.dto.RequestUpdateExam
import com.bobrovskii.exam.data.dto.Token
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ExamApi {

	@POST("/exams")
	suspend fun postExam(@Body requestAddExam: RequestAddExam)

	@POST("/answers/{answerId}/message")
	suspend fun postMessage(@Path("answerId") answerId: Int, @Body requestMessage: RequestMessage)

	@POST("/account/firebase-token")
	suspend fun sendFirebaseToken(@Body token: Token)

	@GET("/account/{accountId}")
	suspend fun getAccountById(@Path("accountId") accountId: Int): AccountDto

	@GET("/exams")
	suspend fun getExams(): List<ExamDto>

	@GET("/exams/{examId}")
	suspend fun getExamById(@Path("examId") examId: Int): ExamDto

	@GET("/exams/{examId}/full")
	suspend fun getFullExamById(@Path("examId") examId: Int, @Query("level") level: Int): FullExamDto

	@GET("/discipline")
	suspend fun getDisciplines(): List<DisciplineDto>

	@GET("/discipline/{disciplineId}")
	suspend fun getDisciplineById(@Path("disciplineId") disciplineId: Int): DisciplineDto

	@GET("/groups")
	suspend fun getGroups(): List<GroupDto>

	@GET("/groups/{groupId}")
	suspend fun getGroupById(@Path("groupId") groupId: Int): GroupDto

	@GET("/answers/{answerId}/full")
	suspend fun getFullAnswerById(@Path("answerId") answerId: Int, @Query("level") level: Int): AnswerInfoDto

	@PUT("/exams")
	suspend fun updateExam(@Body requestUpdateExam: RequestUpdateExam)

	@PUT("/exams/state")
	suspend fun updateExamState(@Body state: RequestExamState)

	@PUT("/answers/state")
	suspend fun updateAnswerRating(@Body requestAnswerRating: RequestAnswerRating)

	@DELETE("/exams/{examId}")
	suspend fun deleteExamById(@Path("examId") examId: Int)
}