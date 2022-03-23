package com.bobrovskii.session.domain.repository

import com.bobrovskii.session.domain.entity.Session

interface LoginRepository {

	suspend fun login(username: String, password: String): Session
}