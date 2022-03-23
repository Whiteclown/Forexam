package com.bobrovskii.exam.data.api

import com.bobrovskii.exam.data.dto.DisciplineDto
import com.bobrovskii.exam.data.dto.ExamDto
import com.bobrovskii.exam.data.dto.ExamRuleDto
import com.bobrovskii.exam.data.dto.GroupDto
import com.bobrovskii.exam.data.dto.PeriodDto
import com.bobrovskii.exam.data.dto.RequestExam
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExamApi {

	@GET("/exam")
	suspend fun getExams(): List<ExamDto>

	@GET("/exam/{examid}/period")
	suspend fun getPeriodsByExam(@Path("examid") id: Int): List<PeriodDto>

	@GET("/teacher/discipline")
	suspend fun getDisciplines(): List<DisciplineDto>

	@GET("/discipline/{disciplineId}/exam-rule")
	suspend fun getExamRulesByDiscipline(@Path("disciplineId") disciplineId: Int): List<ExamRuleDto>

	@GET("/discipline/{disciplineId}/group")
	suspend fun getGroupsByDiscipline(@Path("disciplineId") disciplineId: Int): List<GroupDto>

	@POST("/exam")
	suspend fun postExam(@Body requestExam: RequestExam)
}