package com.teamdefine.signease.application

import android.app.Application
import com.teamdefine.signease.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FirstSign : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FirstSign)
            modules(applicationModules, viewModelModules, useCaseModules, apiModule, networkModule)
        }
    }
}