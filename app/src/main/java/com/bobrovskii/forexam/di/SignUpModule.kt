package com.bobrovskii.forexam.di

import com.bobrovskii.forexam.navigation.Navigator
import com.bobrovskii.signup.presentation.SignUpNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SignUpModule {

	@Provides
	@Singleton
	fun provideSignUpNavigation(navigator: Navigator): SignUpNavigation {
		return navigator
	}
}