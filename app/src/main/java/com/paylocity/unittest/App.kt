package com.paylocity.unittest

import android.app.Application
import com.paylocity.api.apiModule
import com.paylocity.persistence.persistenceModule
import com.paylocity.repository.repositoryModule
import com.paylocity.ui.uiModule
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