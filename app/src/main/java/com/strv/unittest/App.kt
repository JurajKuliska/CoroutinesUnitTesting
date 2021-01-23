package com.strv.unittest

import android.app.Application
import com.strv.api.apiModule
import com.strv.persistence.persistenceModule
import com.strv.repository.repositoryModule
import com.strv.ui.uiModule
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