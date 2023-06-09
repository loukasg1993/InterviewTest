package com.betsson.interviewteest.di

import com.betsson.interviewteest.api.Repository
import com.betsson.interviewteest.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val resultsModule = module{
    single { Repository(get()) }
    viewModel { MainViewModel(get()) }
}