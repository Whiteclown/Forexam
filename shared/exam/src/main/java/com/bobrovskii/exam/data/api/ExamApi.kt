package com.bobrovskii.exam.data.api

import com.bobrovskii.exam.data.dto.DisciplineDto
import com.bobrovskii.exam.data.dto.ExamDto
import com.bobrovskii.exam.data.dto.ExamRuleDto
import com.bobrovskii.exam.data.dto.GroupDto
import com.bobrovskii.exam.data.dto.PeriodDto
import com.bobrovskii.exam.data.dto.RequestExam
import com.bobrovskii.exam.data.dto.RequestPeriodState
import com.bobrovskii.exam.data.dto.RequestTicketsRating
import com.bobrovskii.exam.data.dto.StudentDto
import com.bobrovskii.exam.data.dto.TicketDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExamApi {

	@GET("/exams")
	suspend fun getExams(): List<ExamDto>

	@GET("/exams/{examid}/periods")
	suspend fun getPeriodsByExam(@Path("examid") id: Int): List<PeriodDto>

	@GET("/teacher/discipline")
	suspend fun getDisciplines(): List<DisciplineDto>

	@GET("/discipline/{disciplineId}/exam-rule")
	suspend fun getExamRulesByDiscipline(@Path("disciplineId") disciplineId: Int): List<ExamRuleDto>

	@GET("/discipline/{disciplineId}/group")
	suspend fun getGroupsByDiscipline(@Path("disciplineId") disciplineId: Int): List<GroupDto>

	@POST("/exams")
	suspend fun postExam(@Body requestExam: RequestExam)

	@GET("/discipline/{disciplineId}")
	suspend fun getDisciplineById(@Path("disciplineId") disciplineId: Int): DisciplineDto

	@DELETE("/exams/{examId}")
	suspend fun deleteExamById(@Path("examId") examId: Int)

	@GET("/exams/{examId}")
	suspend fun getExamById(@Path("examId") examId: Int): ExamDto

	@GET("/exam-rule/{id}")
	suspend fun getExamRuleById(@Path("id") examRuleId: Int): ExamRuleDto

	@GET("/exams/{examId}/last-period")
	suspend fun getLastPeriodByExam(@Path("examId") examId: Int): PeriodDto

	@PUT("exams/{examId}")
	suspend fun updateExam(@Path("examId") examId: Int, @Body requestExam: RequestExam)

	@PUT("/periods/{periodId}")
	suspend fun updatePeriodState(@Path("periodId") periodId: Int, @Body state: RequestPeriodState)

	@GET("/exams/{examId}/un-passed")
	suspend fun getUnpassedTickets(@Path("examId") examId: Int): List<TicketDto>

	@GET("/student/{studentId}")
	suspend fun getStudentById(@Path("studentId") studentId: Int): StudentDto

	@PUT("/ticket/rating")
	suspend fun updateTicketsRating(@Body requestTicketsRating: List<RequestTicketsRating>)
}