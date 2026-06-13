package com.example.myapplication.app_event_notification

import kotlinx.coroutines.flow.SharedFlow

interface CoreNotification{
    val storageFlow: SharedFlow<NotificationData>


    fun sendIosMenuEvent(isEvent: Boolean)
    //    fun saveLocale(type: LocaleType)
    //ненужные методы потом почистим
    fun showExitDialogFromIos(isShow: Boolean)
}


data class NotificationData(
    val iosMenuEvent: Boolean,
    val showDialog: Boolean
)