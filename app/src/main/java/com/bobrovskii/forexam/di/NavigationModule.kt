package com.bobrovskii.forexam.di

import com.bobrovskii.addexamination.presentation.AddExamRouter
import com.bobrovskii.answerslist.presentation.AnswersListRouter
import com.bobrovskii.editexamination.presentation.EditExaminationRouter
import com.bobrovskii.forexam.navigation.Navigator
import com.bobrovskii.home.presentation.HomeRouter
import com.bobrovskii.signin.presentation.SignInRouter
import com.bobrovskii.signup.presentation.SignUpRouter
import com.bobrovskii.studentslist.presentation.StudentsListRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import presentation.AnswerRouter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NavigationModule {

	@Provides
	@Singleton
	fun provideNavigator(): Navigator =
		Navigator()

	@Provides
	@Singleton
	fun provideSignInNavigation(navigator: Navigator): SignInRouter =
		navigator

	@Provides
	@Singleton
	fun provideSignUpNavigation(navigator: Navigator): SignUpRouter =
		navigator

	@Provides
	@Singleton
	fun provideHomeNavigation(navigator: Navigator): HomeRouter =
		navigator

	@Provides
	@Singleton
	fun provideAddExamRouter(navigator: Navigator): AddExamRouter =
		navigator

	@Provides
	@Singleton
	fun provideEditExaminationNavigation(navigator: Navigator): EditExaminationRouter =
		navigator

	@Provides
	@Singleton
	fun provideAnswersListRouter(navigator: Navigator): AnswersListRouter =
		navigator

	@Provides
	@Singleton
	fun provideStudentsListRouter(navigator: Navigator): StudentsListRouter =
		navigator

	@Provides
	@Singleton
	fun provideAnswerRouter(navigator: Navigator): AnswerRouter =
		navigator
}