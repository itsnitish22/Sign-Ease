package com.teamdefine.signease.application

import com.teamdefine.signease.di.AppComponent
import com.teamdefine.signease.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class FirstSign : DaggerApplication() {
    lateinit var appComponent: AppComponent
        private set

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder().application(this).build()
        return appComponent
    }
}