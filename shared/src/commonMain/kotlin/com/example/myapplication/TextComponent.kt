package com.example.myapplication

import kotlinx.coroutines.flow.StateFlow

interface TextComponent {

    val state: StateFlow<ProfileTextStore.State>

    fun changeText(text: String)
//    fun interface Factory {
//        operator fun invoke(): TextComponent
//    }
}