package com.bobrovskii.signin.data.storage

import com.bobrovskii.signin.domain.entity.UserCredentials

interface UserCredsDataSource {

	fun getTokenByCredentials(userCredentials: UserCredentials): Boolean
}