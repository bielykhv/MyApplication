package com.example.myapplication

import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultTextComponent(profileTextStoreFactory: ProfileTextStoreFactory): TextComponent {

    private val store  = profileTextStoreFactory.create()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ProfileTextStore.State>
        get() = store.stateFlow

    override fun changeText(text: String) {
       store.accept(ProfileTextStore.Intent.ChangeText(text))
    }

//    class Factory(private val profileTextStoreFactory: ProfileTextStoreFactory): TextComponent.Factory{
//        override fun invoke(): TextComponent {
//            return DefaultTextComponent(profileTextStoreFactory = profileTextStoreFactory )
//        }
//
//    }
}