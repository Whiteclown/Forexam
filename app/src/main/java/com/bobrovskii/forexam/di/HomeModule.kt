package com.bobrovskii.forexam.di

import android.content.Context
import com.bobrovskii.artefact.data.api.ArtefactApi
import com.bobrovskii.artefact.data.datasource.ArtefactDataSource
import com.bobrovskii.artefact.data.datasource.ArtefactDataSourceImpl
import com.bobrovskii.artefact.data.datasource.DocumentDataSource
import com.bobrovskii.artefact.data.datasource.DocumentDataSourceImpl
import com.bobrovskii.artefact.data.repository.ArtefactRepositoryImpl
import com.bobrovskii.artefact.domain.repository.ArtefactRepository
import com.bobrovskii.exam.data.api.ExamApi
import com.bobrovskii.exam.data.repository.ExamRepositoryImpl
import com.bobrovskii.exam.domain.repository.ExamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
	fun provideExamRepository(examApi: ExamApi, @ApplicationContext context: Context): ExamRepository =
		ExamRepositoryImpl(examApi, context)

	@Singleton
	@Provides
	fun provideArtefactApi(@Authorized retrofit: Retrofit): ArtefactApi =
		retrofit.create()

	@Singleton
	@Provides
	fun provideArtefactRepository(
		documentDataSource: DocumentDataSource,
		artefactDataSource: ArtefactDataSource
	): ArtefactRepository =
		ArtefactRepositoryImpl(documentDataSource, artefactDataSource)

	@Singleton
	@Provides
	fun provideDocumentDataSource(@ApplicationContext context: Context): DocumentDataSource =
		DocumentDataSourceImpl(context)

	@Singleton
	@Provides
	fun provideArtefactDataSource(artefactApi: ArtefactApi): ArtefactDataSource =
		ArtefactDataSourceImpl(artefactApi)
}