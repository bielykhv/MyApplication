package com.example.myapplication

import com.example.myapplication.di.mainModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initialize(onKoinStart: KoinApplication.() -> Unit) {
    startKoin {
        onKoinStart()
        modules(
            mainModule
        )
    }
}