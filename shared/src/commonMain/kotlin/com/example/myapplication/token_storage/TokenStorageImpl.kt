package com.example.myapplication.token_storage

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class TokenStorageImpl(private val storage: Settings) : TokenStorage {

    private var _storageFlow = MutableSharedFlow<TokenStoreData>(1)

    override val storageFlow: SharedFlow<TokenStoreData>
        get() = _storageFlow

    override fun sendIosMenuEvent(isEvent: Boolean) {
        _storageFlow.tryEmit( TokenStoreData(
            iosMenuEvent = isEvent
        ))
    }
    private fun updateState() {
        _storageFlow.tryEmit( TokenStoreData(
            iosMenuEvent = getIosEvent()
        ))
    }

    private fun getIosEvent(): Boolean = storage.getBoolean("event", false)
}
