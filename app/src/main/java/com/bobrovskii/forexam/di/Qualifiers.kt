package com.bobrovskii.forexam.di

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class Authorized

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class NotAuthorized