package com.strv.repository

import org.koin.dsl.module

val repositoryModule = module {
    single<ApodRepository> {
        ApodRepositoryImpl(get(), get())
    }
}