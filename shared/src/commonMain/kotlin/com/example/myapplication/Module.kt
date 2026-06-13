package com.example.myapplication

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.myapplication.app_event_notification.CoreNotification
import com.example.myapplication.app_event_notification.CoreNotificationImpl
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            mainModule
        )
    }
}

val mainModule = module {
    viewModel { MyViewModel(get(), get()) }
    factory<StoreFactory> {
        DefaultStoreFactory()
    }
    single<CoreNotification>{ CoreNotificationImpl() }
    factoryOf(::ProfileTextStoreFactory)
    factory<TextComponent> { DefaultTextComponent(get()) }
}