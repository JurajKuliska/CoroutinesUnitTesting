package com.apod.unittest

import android.app.Application
import com.apod.api.apiModule
import com.apod.persistence.persistenceModule
import com.apod.repository.repositoryModule
import com.apod.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(
                uiModule,
                persistenceModule,
                apiModule,
                repositoryModule
            )
        }
    }
}