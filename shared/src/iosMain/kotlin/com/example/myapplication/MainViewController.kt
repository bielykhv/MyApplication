package com.example.myapplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import com.example.myapplication.app_event_notification.CoreNotification
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.UIKit.UIViewController
import kotlin.getValue


@Suppress("unused") // Called from Swift
fun MainViewController(topLevelRoute: TopLevelRoute): UIViewController = ComposeUIViewController(
    configure = { onFocusBehavior = OnFocusBehavior.DoNothing },
) {
    App(topLevelRoute)
}

@Suppress("unused") // Called from Swift
fun MainViewController(
    topLevelRoute: TopLevelRoute,
    onNavigate: (AppRoute) -> Unit,
//    onGoBack: () -> Unit,
//    onSet: (AppRoute) -> Unit,
    onActivate: (TopLevelRoute) -> Unit,
): UIViewController = ComposeUIViewController(
    configure = { onFocusBehavior = OnFocusBehavior.DoNothing },
) {
    CompositionLocalProvider(
        localUseNativeNavigation provides true,
    ) {
        Box(
            Modifier
                .fillMaxSize()
        ) {
            App(
                topLevelRoute = topLevelRoute,
                onNavigate = onNavigate,
                onActivate = onActivate,
            )
        }
    }
}

@Suppress("unused") // Called from Swift
fun ScreenViewController(
    route: AppRoute,
    onNavigate: (AppRoute) -> Unit,
    onGoBack: () -> Unit,
    onSet: (AppRoute) -> Unit,
    onActivate: (TopLevelRoute) -> Unit,
): UIViewController = ComposeUIViewController(
    configure = { onFocusBehavior = OnFocusBehavior.DoNothing },
) {

    CompositionLocalProvider(
        localUseNativeNavigation provides true,
    ) {
        Box(
            Modifier
                .fillMaxSize()
        ) {
            SingleScreenApp(
                route = route,
                onNavigate = onNavigate,
                onGoBack = onGoBack,
                onSet = onSet,
                onActivate = onActivate,
            )
        }
    }
}
@Suppress("unused") // Called from Swift
class IosEventHandler : KoinComponent {
    private val coreNotification: CoreNotification by inject()
    fun sendNotification(isEvent: Boolean){
        coreNotification.sendIosMenuEvent(isEvent)
    }
    fun showDialog(isEvent: Boolean){
        coreNotification.showExitDialogFromIos(isEvent)
    }
}
