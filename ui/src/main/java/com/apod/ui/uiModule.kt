package com.apod.ui

import com.apod.ui.apod.ApodViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        ApodViewModel(get())
    }
}