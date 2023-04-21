package com.teamdefine.signease.di

import com.teamdefine.signease.confirmation.ConfirmationViewModel
import com.teamdefine.signease.homepage.HomeFragmentViewModel
import com.teamdefine.signease.loginandregister.RegisterViewModel
import com.teamdefine.signease.templates.TemplateListViewModel
import org.koin.dsl.module

val viewModelModules = module {
    factory { HomeFragmentViewModel() }
    factory { ConfirmationViewModel() }
    factory { TemplateListViewModel() }
    factory { RegisterViewModel() }
}