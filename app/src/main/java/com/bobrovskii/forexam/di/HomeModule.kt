package com.bobrovskii.forexam.di

import com.bobrovskii.forexam.navigation.Navigator
import com.bobrovskii.home.presentation.HomeNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HomeModule {

	@Provides
	@Singleton
	fun provideHomeNavigation(navigator: Navigator): HomeNavigation {
		return navigator
	}
}