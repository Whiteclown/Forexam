package com.bobrovskii.session.data.repository

import android.content.Context
import com.bobrovskii.session.domain.entity.Session
import com.bobrovskii.session.domain.repository.SessionRepository
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
	//@ioDispatcher private val ioDispatcher: CoroutineDispatcher,
	@ApplicationContext private val context: Context
) : SessionRepository {

	private companion object {

		const val SESSION_PREFERENCES = "sessionPreferences"
		const val SESSION_VALUE = "sessionValue"
	}

	private val sessionPreferences by lazy {
		context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE)
	}

	private val sessionAdapter by lazy {
		val moshi = Moshi.Builder().build()
		moshi.adapter(Session::class.java)
	}

	override fun save(session: Session) {
		sessionPreferences
			.edit()
			.putString(SESSION_VALUE, sessionAdapter.toJson(session))
			.apply()
	}

	override fun get(): Session =
		sessionPreferences.getString(SESSION_VALUE, null)
			?.let(sessionAdapter::fromJson)
			?: throw Exception("Session is not found")

	override suspend fun clear() {
		sessionPreferences
			.edit()
			.clear()
			.apply()

	}
}