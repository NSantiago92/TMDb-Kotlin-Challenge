package com.nsantiago.tmdbkotlinchallenge

import android.app.Application
import com.nsantiago.tmdbkotlinchallenge.utils.serviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(serviceModule)
        }
    }
}