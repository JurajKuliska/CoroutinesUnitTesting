package com.strv.persistence

import com.strv.persistence.dao.ApodDb
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val persistenceModule = module {

    single<ApodPersistence> {
        ApodPersistenceImpl(
            ApodDb.getInstance(androidContext()).apodDao()
        )
    }
}