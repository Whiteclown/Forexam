package com.bobrovskii.signin.domain.usecase

import com.bobrovskii.signin.domain.entity.UserCredentials
import com.bobrovskii.signin.domain.repository.UserRepository

class GetTokenUseCase (private val repository: UserRepository) {
	operator fun invoke(userCredentials: UserCredentials): Boolean = repository.getTokenByCredentials(userCredentials = userCredentials)
}