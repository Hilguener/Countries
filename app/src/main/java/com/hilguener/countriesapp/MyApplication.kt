package com.hilguener.countriesapp

import android.app.Application
import com.hilguener.countriesapp.di.appModules
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                appModules
            )
        }
    }
}
