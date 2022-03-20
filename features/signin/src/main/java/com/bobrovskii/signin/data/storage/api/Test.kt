package com.bobrovskii.signin.data.storage.api

import com.bobrovskii.signin.data.storage.UserCredsDataSource
import com.bobrovskii.signin.domain.entity.UserCredentials

class Test : UserCredsDataSource {

	override fun getTokenByCredentials(userCredentials: UserCredentials): Boolean {
		return true
	}
}