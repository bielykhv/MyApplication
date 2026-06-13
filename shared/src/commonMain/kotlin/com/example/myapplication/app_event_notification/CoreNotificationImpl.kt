package com.example.myapplication.app_event_notification

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class CoreNotificationImpl : CoreNotification {

    private var _storageFlow = MutableSharedFlow<NotificationData>(1)

    override val storageFlow: SharedFlow<NotificationData>
        get() = _storageFlow

    override fun sendIosMenuEvent(isEvent: Boolean) {
        _storageFlow.tryEmit( NotificationData(
            iosMenuEvent = isEvent,
            showDialog = false
        ))
    }

    override fun showExitDialogFromIos(isShow: Boolean) {
        _storageFlow.tryEmit( NotificationData(
            iosMenuEvent = false,
            showDialog = isShow
        ))
    }
}
