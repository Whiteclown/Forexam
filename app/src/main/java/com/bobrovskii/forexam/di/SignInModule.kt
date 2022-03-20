package com.bobrovskii.forexam.di

import com.bobrovskii.forexam.navigation.Navigator
import com.bobrovskii.signin.data.repository.UserRepositoryImpl
import com.bobrovskii.signin.data.storage.UserCredsDataSource
import com.bobrovskii.signin.data.storage.api.Test
import com.bobrovskii.signin.domain.repository.UserRepository
import com.bobrovskii.signin.domain.usecase.GetTokenUseCase
import com.bobrovskii.signin.presentation.SignInNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SignInModule {

	@Provides
	@Singleton
	fun provideUserCredsDataSource(): UserCredsDataSource {
		return Test()
	}

	@Provides
	@Singleton
	fun provideSignInNavigation(navigator: Navigator): SignInNavigation {
		return navigator
	}

	@Singleton
	@Provides
	fun provideUserRepository(userCredsDataSource: UserCredsDataSource): UserRepository {
		return UserRepositoryImpl(userCredsDataSource = userCredsDataSource)
	}

	@Provides
	fun provideGetTokenUseCase(userRepository: UserRepository): GetTokenUseCase {
		return GetTokenUseCase(repository = userRepository)
	}
}