package com.bobrovskii.session.domain.repository

import com.bobrovskii.session.domain.entity.Session

interface SessionRepository {

	fun save(session: Session)

	fun get(): Session

	suspend fun clear()
}