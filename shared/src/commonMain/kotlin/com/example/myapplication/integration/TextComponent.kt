package com.example.myapplication.integration

import kotlinx.coroutines.flow.StateFlow

interface TextComponent {

    val state: StateFlow<ProfileTextStore.State>

    fun changeText(text: String)

    fun showDialog(isShow: Boolean)
}