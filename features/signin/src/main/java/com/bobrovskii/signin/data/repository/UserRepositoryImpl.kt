package com.bobrovskii.signin.data.repository

import com.bobrovskii.signin.data.storage.UserCredsDataSource
import com.bobrovskii.signin.domain.entity.UserCredentials
import com.bobrovskii.signin.domain.repository.UserRepository

class UserRepositoryImpl(private val userCredsDataSource: UserCredsDataSource) : UserRepository {

	override fun getTokenByCredentials(userCredentials: UserCredentials): Boolean {
		return userCredsDataSource.getTokenByCredentials(userCredentials)
	}
}