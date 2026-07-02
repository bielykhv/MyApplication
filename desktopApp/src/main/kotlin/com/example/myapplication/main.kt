package com.example.myapplication

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.myapplication.app_routes.Home
import di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "MyApplication",
    ) {
        App(
            topLevelRoute = Home
        )
    }
}