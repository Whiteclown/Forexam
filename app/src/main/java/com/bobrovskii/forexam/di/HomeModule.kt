package com.bobrovskii.forexam.di

import com.bobrovskii.exam.data.api.ExamApi
import com.bobrovskii.exam.data.repository.ExamRepositoryImpl
import com.bobrovskii.exam.domain.repository.ExamRepository
import com.bobrovskii.exam.domain.usecase.GetDisciplinesUseCase
import com.bobrovskii.exam.domain.usecase.GetExamRulesByDisciplineUseCase
import com.bobrovskii.exam.domain.usecase.GetExamsUseCase
import com.bobrovskii.exam.domain.usecase.GetGroupsByDisciplineUseCase
import com.bobrovskii.exam.domain.usecase.GetPeriodsByExamIdUseCase
import com.bobrovskii.exam.domain.usecase.PostExamUseCase
import com.bobrovskii.forexam.navigation.Navigator
import com.bobrovskii.home.presentation.navigation.AddExamNavigation
import com.bobrovskii.home.presentation.navigation.HomeNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HomeModule {

	@Provides
	@Singleton
	fun provideAddExamNavigation(navigator: Navigator): AddExamNavigation =
		navigator

	@Provides
	@Singleton
	fun provideHomeNavigation(navigator: Navigator): HomeNavigation =
		navigator

	@Singleton
	@Provides
	fun provideExamApi(@Authorized retrofit: Retrofit): ExamApi =
		retrofit.create()

	@Singleton
	@Provides
	fun provideExamRepository(examApi: ExamApi): ExamRepository =
		ExamRepositoryImpl(api = examApi)

	@Provides
	fun provideGetExamsUseCase(examRepository: ExamRepository): GetExamsUseCase =
		GetExamsUseCase(repository = examRepository)

	@Provides
	fun provideGetPeriodsByExamIdUseCase(examRepository: ExamRepository): GetPeriodsByExamIdUseCase =
		GetPeriodsByExamIdUseCase(repository = examRepository)

	@Provides
	fun provideGetDisciplinesUseCase(examRepository: ExamRepository): GetDisciplinesUseCase =
		GetDisciplinesUseCase(repository = examRepository)

	@Provides
	fun provideGetExamRulesByDisciplineUseCase(examRepository: ExamRepository): GetExamRulesByDisciplineUseCase =
		GetExamRulesByDisciplineUseCase(repository = examRepository)

	@Provides
	fun provideGetGroupsByDisciplineUseCase(examRepository: ExamRepository): GetGroupsByDisciplineUseCase =
		GetGroupsByDisciplineUseCase(repository = examRepository)

	@Provides
	fun providePostExamUseCase(examRepository: ExamRepository): PostExamUseCase =
		PostExamUseCase(repository = examRepository)
}