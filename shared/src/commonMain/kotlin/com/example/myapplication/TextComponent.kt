package com.example.myapplication

import kotlinx.coroutines.flow.StateFlow

interface TextComponent {

    val state: StateFlow<ProfileTextStore.State>

    fun changeText(text: String)

    fun showDialog(isShow: Boolean)
//    fun interface Factory {
//        operator fun invoke(): TextComponent
//    }
}