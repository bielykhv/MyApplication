package com.example.myapplication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun App(
    topLevelRoute: TopLevelRoute,
    onNavigate: ((AppRoute) -> Unit)? = null,
    onActivate: ((TopLevelRoute) -> Unit)? = null,
) {
    CompositionLocalProvider(
        localUseNativeNavigation provides true
    ) {
        val startRoute: AppRoute = remember { topLevelRoute }
        NavHost(startRoute = startRoute, onActivate = onActivate, onNavigate = onNavigate)
    }
}
val localUseNativeNavigation = staticCompositionLocalOf { false }

@Composable
internal fun NavHost(
    startRoute: AppRoute,
    onNavigate: ((AppRoute) -> Unit)? = null,
    onActivate: ((TopLevelRoute) -> Unit)? = null,
) {
    val navState = rememberNavState(
        startRoute = startRoute,
        topLevelRoutes = setOf(
            Home,
            Search,
            Profile
        ),
        primaryTopLevelRoute = Home,
    )
    val topLevelBackEnabled = remember { false }
    println(topLevelBackEnabled)
    val navigator = remember(navState, topLevelBackEnabled) {
        Navigator(navState, topLevelBackEnabled)
    }

    if (onNavigate != null) {
        LaunchedEffect(navState) {
            snapshotFlow { navState.currentBackstack.toList() }.collect { backstack: List<AppRoute> ->
                val detailRoutes = backstack.drop(1)
                if (detailRoutes.isNotEmpty()) {
                    detailRoutes.forEach { onNavigate(it) }
                    navState.currentBackstack.removeRange(1, navState.currentBackstack.size)
                }
            }
        }
    }

    if (onActivate != null) {
        LaunchedEffect(navState) {
            snapshotFlow { navState.topLevelRoute }.collect { route: TopLevelRoute? ->
                if (route != null) onActivate(route)
            }
        }
    }

    val entryProvider = entryProvider {
        screens(
            navigator = navigator,
            onBack = { navigator.goBack() },
        )
    }
    val useNativeNavigation = remember { true }

    CompositionLocalProvider(localUseNativeNavigation provides useNativeNavigation) {
        Box() {
            val content = @Composable {
                NavDisplay(
                    entries = navState.toDecoratedEntries(entryProvider),
                    onBack = { navigator.goBack() },
                )
            }
            if (useNativeNavigation) {
                content()
            } else {
                NavScaffold(
                    navState = navState,
                    navigator = navigator,
                    content = content,
                )
            }
        }
    }

}




@Composable
internal fun NavScaffold(
    navState: NavState,
    navigator: Navigator,
    content: @Composable (() -> Unit)
) {
    val onSelectRoute: (TopLevelRoute) -> Unit = { route ->
        navigator.activate(route)
    }
    val destinations = bottomNavDestinations
    Row(
        Modifier.fillMaxSize()

    ) {
        val enterAnimSpec = tween<IntSize>(delayMillis = AnimationConstants.DefaultDurationMillis)

        Column(Modifier.weight(1f)) {
            Box(Modifier.weight(1f).clipToBounds()) {
                content()
            }
            AnimatedVisibility(
                visible = true,
                enter = expandVertically(enterAnimSpec),
                exit = shrinkVertically() + fadeOut(),
            ) {
                BottomNavigation(
                    currentRoute = navState.topLevelRoute,
                    destinations = destinations,
                    onSelectRoute = onSelectRoute,
                )
            }
        }
    }
}

@Composable
private fun BottomNavigation(
    currentRoute: TopLevelRoute?,
    destinations: List<MainNavDestination<TopLevelRoute>>,
    onSelectRoute: (TopLevelRoute) -> Unit,
) {
    val currentDestination = destinations.find { it.route == currentRoute }

    Column(
    ) {
        MainNavigationBar(
            currentDestination = currentDestination,
            destinations = destinations,
            onSelect = { selectedDestination ->
                onSelectRoute(selectedDestination.route)
            },
        )
    }
}
@Composable
fun <T : Any> MainNavigationBar(
    currentDestination: MainNavDestination<T>?,
    destinations: List<MainNavDestination<T>>,
    onSelect: (MainNavDestination<T>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        destinations.forEach { destination ->
            MainNavigationButton(
                iconResource = destination.icon,
                iconFilledResource = destination.iconSelected,
                contentDescription = destination.label,
                selected = destination == currentDestination,
                onClick = { onSelect(destination) },
                modifier = Modifier
                    .widthIn(max = 150.dp)
                    .fillMaxWidth()
                    .weight(1f, fill = false),
            )
        }
    }
}
@Composable
private fun MainNavigationButton(
    iconResource: DrawableResource,
    iconFilledResource: DrawableResource,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column (modifier = modifier.selectable(
        selected = selected,
        enabled = true,
        role = Role.Tab,
        onClick = onClick,

    )
        .background(if (selected) MaterialTheme.colorScheme.errorContainer else Color.Transparent),
    ){
        Image(
            modifier = modifier.padding(1.dp).size(28.dp),
            painter = painterResource(if (selected) iconFilledResource else iconResource),
            contentDescription = contentDescription,
        )
        Text(contentDescription)
    }

}



@Composable
fun rememberNavState(
    startRoute: AppRoute,
    primaryTopLevelRoute: TopLevelRoute,
    topLevelRoutes: Set<TopLevelRoute>,
): NavState {

    val topLevelRoute = rememberSerializable(
        startRoute, topLevelRoutes,
        serializer = MutableStateSerializer<TopLevelRoute?>(),
    ) {
        mutableStateOf(startRoute as? TopLevelRoute)
    }

    val topLevelBackstacks: Map<TopLevelRoute, SnapshotStateList<AppRoute>> = buildMap {
        topLevelRoutes.forEach { route ->
            put(route, rememberSerializable(serializer = SnapshotStateListSerializer()) {
                mutableStateListOf(route)
            })
        }
    }

    val defaultBackstack = rememberSerializable(serializer = SnapshotStateListSerializer()) {
        if (startRoute !is TopLevelRoute) {
            mutableStateListOf(startRoute)
        } else {
            mutableStateListOf()
        }
    }

    val currentBackstack = rememberSerializable(serializer = SnapshotStateListSerializer()) {
        mutableStateListOf<AppRoute>()
    }

    return remember(startRoute, topLevelRoutes) {
        NavState(
            _topLevelRoute = topLevelRoute,
            primaryTopLevelRoute = primaryTopLevelRoute,
            topLevelBackStacks = topLevelBackstacks,
            defaultBackstack = defaultBackstack,
            currentBackstack = currentBackstack,
        )
    }
}

class NavState(
    private val _topLevelRoute: MutableState<TopLevelRoute?>,
    val topLevelBackStacks: Map<TopLevelRoute, SnapshotStateList<AppRoute>>,
    val defaultBackstack: SnapshotStateList<AppRoute>,
    val primaryTopLevelRoute: TopLevelRoute,
    val currentBackstack: SnapshotStateList<AppRoute>,
) {

    init {
        if (currentBackstack.isEmpty()) {
            val source = if (_topLevelRoute.value != null) {
                topLevelBackStacks[_topLevelRoute.value]!!
            } else {
                defaultBackstack
            }
            currentBackstack.addAll(source)
        }
    }

    var topLevelRoute: TopLevelRoute?
        get() = _topLevelRoute.value
        set(value) {
            val oldRoute = _topLevelRoute.value

            // Save current backstack to the old route's storage
            val oldStorage =
                if (oldRoute != null) topLevelBackStacks[oldRoute]!! else defaultBackstack
            oldStorage.clear()
            oldStorage.addAll(currentBackstack)

            _topLevelRoute.value = value

            // Load new route's backstack into currentBackstack
            val newStorage = if (value != null) topLevelBackStacks[value]!! else defaultBackstack
            currentBackstack.clear()
            currentBackstack.addAll(newStorage)
        }

    @Composable
    fun toDecoratedEntries(
        entryProvider: (AppRoute) -> NavEntry<AppRoute>
    ): SnapshotStateList<NavEntry<AppRoute>> {
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<AppRoute>(),
        )

        val topLevelEntries = topLevelBackStacks
            .mapValues { (route, stack) ->
                rememberDecoratedNavEntries(
                    backStack = if (route == topLevelRoute) currentBackstack else stack,
                    entryDecorators = decorators,
                    entryProvider = entryProvider
                )
            }
            .withDefault { emptyList() }

        val defaultEntries = rememberDecoratedNavEntries(
            backStack = if (topLevelRoute == null) currentBackstack else defaultBackstack,
            entryDecorators = decorators,
            entryProvider = entryProvider,
        )

        return when (val topRoute = topLevelRoute) {
            null -> defaultEntries
            primaryTopLevelRoute -> topLevelEntries.getValue(primaryTopLevelRoute)
            else -> topLevelEntries.getValue(primaryTopLevelRoute) + topLevelEntries.getValue(
                topRoute
            )
        }.toMutableStateList()
    }
}


@Serializable
data class Flags(
    val enableBackOnTopLevelScreens: Boolean = true
)

val LocalFlags = compositionLocalOf<Flags> {
    error("LocalFlags must be part of the call hierarchy to provide configuration")
}