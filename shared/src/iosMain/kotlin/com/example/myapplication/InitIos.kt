package com.example.myapplication

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