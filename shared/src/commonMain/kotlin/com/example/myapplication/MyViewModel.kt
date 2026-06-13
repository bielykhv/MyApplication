package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.token_storage.TokenStorage
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.instancekeeper.getStore


class MyViewModel(private val tokenStorage: TokenStorage,  val textComponent: TextComponent): ViewModel() {



    val state: StateFlow<ProfileTextStore.State> = textComponent.state


    init {
        println("$this")
        viewModelScope.launch {
            tokenStorage.storageFlow.collect {
                if (it.iosMenuEvent) {
                    menuClick()
                }
            }
        }
    }

    fun menuClick(){
        textComponent.changeText("text")
        tokenStorage.sendIosMenuEvent(false)
//        _state.value = "text"
//        tokenStorage.sendIosMenuEvent(false)
    }

    fun reset(){
        textComponent.changeText("new text")
        tokenStorage.sendIosMenuEvent(false)
    }

    override fun onCleared() {
        super.onCleared()
        println("on cleared")
    }

}