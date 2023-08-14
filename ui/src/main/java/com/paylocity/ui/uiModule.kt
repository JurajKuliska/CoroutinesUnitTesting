package com.paylocity.ui

import com.paylocity.ui.apod.ApodViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        ApodViewModel(get())
    }
}