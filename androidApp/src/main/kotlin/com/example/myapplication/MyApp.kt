package com.example.myapplication

import android.app.Application
import org.koin.android.ext.koin.androidContext

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApp)
        }
    }
}