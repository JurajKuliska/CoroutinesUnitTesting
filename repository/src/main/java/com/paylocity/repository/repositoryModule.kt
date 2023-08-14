package com.paylocity.repository

import org.koin.dsl.module

val repositoryModule = module {
    single<ApodRepository> {
        ApodRepositoryImpl(get(), get())
    }
}