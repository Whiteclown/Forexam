package com.bobrovskii.forexam.di

import com.bobrovskii.addexamination.presentation.AddExamRouter
import com.bobrovskii.editexamination.presentation.EditExaminationNavigation
import com.bobrovskii.forexam.navigation.Navigator
import com.bobrovskii.home.presentation.navigation.HomeNavigation
import com.bobrovskii.progressexamination.presentation.ProgressExaminationRouter
import com.bobrovskii.signin.presentation.SignInNavigation
import com.bobrovskii.signup.presentation.SignUpNavigation
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
	fun provideSignInNavigation(navigator: Navigator): SignInNavigation =
		navigator

	@Provides
	@Singleton
	fun provideSignUpNavigation(navigator: Navigator): SignUpNavigation =
		navigator

	@Provides
	@Singleton
	fun provideHomeNavigation(navigator: Navigator): HomeNavigation =
		navigator

	@Provides
	@Singleton
	fun provideAddExamRouter(navigator: Navigator): AddExamRouter =
		navigator

	@Provides
	@Singleton
	fun provideEditExaminationNavigation(navigator: Navigator): EditExaminationNavigation =
		navigator

	@Provides
	@Singleton
	fun provideProgressExaminationRouter(navigator: Navigator): ProgressExaminationRouter =
		navigator

	@Provides
	@Singleton
	fun provideAnswerRouter(navigator: Navigator): AnswerRouter =
		navigator
}