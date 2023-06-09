package com.betsson.interviewteest.di

import com.betsson.interviewteest.api.ApiClient
import org.koin.dsl.module

val networkModule = module {
    single { ApiClient.create() }
}