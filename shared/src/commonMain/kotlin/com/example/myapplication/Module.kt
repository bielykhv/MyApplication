package com.example.myapplication

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.myapplication.token_storage.TokenStorage
import com.example.myapplication.token_storage.TokenStorageImpl
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.core.module.dsl.viewModelOf
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
    single<TokenStorage> { TokenStorageImpl(getStorageForToken()) }
    factory<StoreFactory> {
        DefaultStoreFactory()
    }
    factoryOf(::ProfileTextStoreFactory)
    factory<TextComponent> { DefaultTextComponent(get()) }
}