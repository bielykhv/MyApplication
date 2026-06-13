package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.app_event_notification.CoreNotification
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MyViewModel(private val coreNotification: CoreNotification,  val textComponent: TextComponent): ViewModel() {



    val state: StateFlow<ProfileTextStore.State> = textComponent.state


    init {
        println("$this")
        viewModelScope.launch {
            coreNotification.storageFlow.collect {
                if (it.iosMenuEvent) {
                    menuClick()
                }
                if(it.showDialog) textComponent.showDialog(true)
            }
        }
    }

    fun menuClick(){
        textComponent.changeText("Текст изменен")
        coreNotification.sendIosMenuEvent(false)
    }

    fun reset(){
        textComponent.changeText("текст сброшен")
        coreNotification.sendIosMenuEvent(false)
    }


    fun hideDialog(){
        textComponent.showDialog(false)
    }
    fun resetWhenScreenResumes(){
        textComponent.changeText("экран resumes")
        coreNotification.sendIosMenuEvent(false)
    }

    override fun onCleared() {
        super.onCleared()
        println("on cleared")
    }

}