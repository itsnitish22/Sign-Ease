package com.teamdefine.signease.di

import com.teamdefine.signease.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun onMainActivity(): MainActivity
}