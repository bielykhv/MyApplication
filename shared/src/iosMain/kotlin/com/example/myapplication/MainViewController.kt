package com.example.myapplication

import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import com.example.myapplication.app_event_notification.CoreNotification
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.UIKit.UIViewController
import kotlin.getValue

@Suppress("unused") // Called from Swift
fun MainViewController(
    topLevelRoute: TopLevelRoute,
    onNavigate: (AppRoute) -> Unit,
    onActivate: (TopLevelRoute) -> Unit,
) = ComposeUIViewController(configure = { onFocusBehavior = OnFocusBehavior.DoNothing }) {
    App(
        topLevelRoute = topLevelRoute,
        onNavigate = onNavigate,
        onActivate = onActivate,
    )
}

@Suppress("unused") // Called from Swift
fun MainViewController(topLevelRoute: TopLevelRoute) = ComposeUIViewController(
    configure = { onFocusBehavior = OnFocusBehavior.DoNothing },
) {
    App(topLevelRoute)
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
    SingleScreenApp(
        route = route,
        onNavigate = onNavigate,
        onGoBack = onGoBack,
        onSet = onSet,
        onActivate = onActivate,
    )
}
@Suppress("unused") // Called from Swift
class IosEventHandler : KoinComponent {
    private val coreNotification: CoreNotification by inject()
    fun sendNotification(isEvent: Boolean){
        coreNotification.sendIosMenuEvent(isEvent)
    }
}
