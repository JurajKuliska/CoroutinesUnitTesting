package com.strv.persistence

import com.strv.persistence.dao.ApodDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val persistenceModule = module {

    single<ApodPersistence> {
        ApodPersistenceImpl(
            ApodDb.getInstance(androidContext()).apodDao()
        )
    }
}