package com.betsson.interviewteest.common

import android.app.Application
import com.betsson.interviewteest.di.networkModule
import com.betsson.interviewteest.di.resultsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class KotlinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KotlinApplication)
            modules(networkModule, resultsModule)
        }
    }
}
