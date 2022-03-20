package com.bobrovskii.signin.domain.repository

import com.bobrovskii.signin.domain.entity.UserCredentials

interface UserRepository {

	fun getTokenByCredentials(userCredentials: UserCredentials): Boolean
}