package com.bobrovskii.forexam.di

import android.content.Context
import com.bobrovskii.forexam.navigation.Navigator
import com.bobrovskii.session.data.api.SessionApi
import com.bobrovskii.session.data.repository.LoginRepositoryImpl
import com.bobrovskii.session.data.repository.SessionRepositoryImpl
import com.bobrovskii.session.domain.repository.LoginRepository
import com.bobrovskii.session.domain.repository.SessionRepository
import com.bobrovskii.session.domain.usecase.LoginUseCase
import com.bobrovskii.signin.presentation.SignInNavigation
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
class SignInModule {

	@Provides
	@Singleton
	fun provideSignInNavigation(navigator: Navigator): SignInNavigation {
		return navigator
	}

	@Singleton
	@Provides
	fun provideSessionApi(@NotAuthorized retrofit: Retrofit): SessionApi =
		retrofit.create()

	@Singleton
	@Provides
	fun provideLoginRepository(api: SessionApi): LoginRepository {
		return LoginRepositoryImpl(api = api)
	}

	@Singleton
	@Provides
	fun provideSessionRepository(@ApplicationContext context: Context): SessionRepository {
		return SessionRepositoryImpl(context)
	}

	@Provides
	fun provideLoginUseCase(loginRepository: LoginRepository, sessionRepository: SessionRepository): LoginUseCase {
		return LoginUseCase(loginRepository = loginRepository, sessionRepository = sessionRepository)
	}
}