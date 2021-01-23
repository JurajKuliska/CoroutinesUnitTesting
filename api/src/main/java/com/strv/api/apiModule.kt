package com.strv.api

import org.koin.dsl.module

val apiModule = module {
    single<ApodApi> {
        ApodApiImpl()
    }
}