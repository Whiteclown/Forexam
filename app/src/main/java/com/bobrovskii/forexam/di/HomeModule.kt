package com.bobrovskii.forexam.di

import com.bobrovskii.exam.data.api.ExamApi
import com.bobrovskii.exam.data.repository.ExamRepositoryImpl
import com.bobrovskii.exam.domain.repository.ExamRepository
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

	@Singleton
	@Provides
	fun provideExamApi(@Authorized retrofit: Retrofit): ExamApi =
		retrofit.create()

	@Singleton
	@Provides
	fun provideExamRepository(examApi: ExamApi): ExamRepository =
		ExamRepositoryImpl(api = examApi)
}