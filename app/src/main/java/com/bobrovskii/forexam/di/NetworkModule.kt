package com.bobrovskii.forexam.di

import android.content.Context
import com.bobrovskii.session.data.interceptor.NetworkConnectionInterceptor
import com.bobrovskii.session.data.interceptor.SessionInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	const val BASE_URL = "http://217.71.129.139:4502"

	@Provides
	@Singleton
	fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
		HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.BODY
		}

	@Provides
	@Singleton
	fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context): NetworkConnectionInterceptor =
		NetworkConnectionInterceptor(context)

	@Provides
	@Singleton
	@Authorized
	fun provideAuthorizedOkHttpClient(
		httpLoggingInterceptor: HttpLoggingInterceptor,
		sessionInterceptor: SessionInterceptor,
		networkConnectionInterceptor: NetworkConnectionInterceptor,
	): OkHttpClient =
		OkHttpClient
			.Builder()
			.addInterceptor(httpLoggingInterceptor)
			.addInterceptor(sessionInterceptor)
			.addInterceptor(networkConnectionInterceptor)
			.build()

	@Provides
	@Singleton
	@NotAuthorized
	fun provideNotAuthorizedOkHttpClient(
		httpLoggingInterceptor: HttpLoggingInterceptor,
		networkConnectionInterceptor: NetworkConnectionInterceptor,
	): OkHttpClient =
		OkHttpClient
			.Builder()
			.addInterceptor(httpLoggingInterceptor)
			.addInterceptor(networkConnectionInterceptor)
			.build()

	@Provides
	@Singleton
	@NotAuthorized
	fun provideNotAuthorizedRetrofit(@NotAuthorized client: OkHttpClient): Retrofit =
		Retrofit
			.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.client(client)
			.build()

	@Provides
	@Singleton
	@Authorized
	fun provideAuthorizedRetrofit(@Authorized client: OkHttpClient): Retrofit =
		Retrofit
			.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(MoshiConverterFactory.create())
			.client(client)
			.build()

	private val moshi = Moshi
		.Builder()
		.add(KotlinJsonAdapterFactory())
		.build()
}