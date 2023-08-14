package com.apod.repository

import org.koin.dsl.module

val repositoryModule = module {
    single<ApodRepository> {
        ApodRepositoryImpl(get(), get())
    }
}