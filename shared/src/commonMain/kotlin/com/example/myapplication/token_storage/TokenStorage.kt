package com.example.myapplication.token_storage

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TokenStorage {
    val storageFlow: SharedFlow<TokenStoreData>


    fun sendIosMenuEvent(isEvent: Boolean)
    //    fun saveLocale(type: LocaleType)
    //ненужные методы потом почистим
}


data class TokenStoreData(
    val iosMenuEvent: Boolean
)