package com.example.myapplication

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.myapplication.ProfileTextStore.Intent
import com.example.myapplication.ProfileTextStore.Label
import com.example.myapplication.ProfileTextStore.State

interface ProfileTextStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeText(val text: String): Intent
    }

    data class State(
        val text: String = ""
    )

    sealed interface Label {
    }
}

 class ProfileTextStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): ProfileTextStore =
        object : ProfileTextStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ProfileTextStore",
            initialState = State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data class ChangeText(val text: String): Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when(intent){
                is Intent.ChangeText -> dispatch(Msg.ChangeText(intent.text))
            }
        }

        override fun executeAction(action: Action) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeText -> copy(text = msg.text)
            }
    }
}
