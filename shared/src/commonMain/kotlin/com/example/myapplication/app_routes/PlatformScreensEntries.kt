package com.example.myapplication.app_routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import com.example.myapplication.localUseNativeNavigation
import com.example.myapplication.screens.HomeDetailsScreen
import com.example.myapplication.screens.HomeScreen
import com.example.myapplication.screens.ProfileDetailsNextScreen
import com.example.myapplication.screens.ProfileDetailsNextTextScreen
import com.example.myapplication.screens.ProfileDetailsScreen
import com.example.myapplication.screens.ProfileScreen
import com.example.myapplication.screens.SearchDetailsScreen
import com.example.myapplication.screens.SearchScreen

fun EntryProviderScope<AppRoute>.screens(
    navigator: Navigator,
    onBack: () -> Unit,
) {
    entry<Home> {
        HomeScreen(
            openDetails = { navigator.add(HomeDetails()) }
        )
    }
    entry<Profile> {
        ProfileScreen(
            openDetails = { navigator.add(ProfileDetails()) }
        )
    }
    entry<Search> {
        SearchScreen(
            openDetails = {
                navigator.add(SearchDetails())
            }
        )
    }
    entry<ProfileDetails> {
        ProfileDetailsScreen(
            onBack = onBack,
            openDetails = { navigator.add(ProfileDetailsNext()) }
        )
    }
    entry<SearchDetails> {
        SearchDetailsScreen(
            onBack = onBack,
            openDetails = {
                navigator.add(SearchDetails())
            }
        )
    }
    entry<HomeDetails> {
        HomeDetailsScreen(
            onBack = onBack,
            openDetails = { navigator.add(HomeDetails()) }
        )
    }
    entry<ProfileDetailsNext> {
        ProfileDetailsNextScreen(
            onBack = onBack,
            openDetails = { navigator.add(ProfileDetailsNextText()) },
        )


    }
    entry<ProfileDetailsNextText> {
        ProfileDetailsNextTextScreen(
            onBack = onBack,
            openDetails = {
                navigator.crossTab(SearchDetails())
            },
        )
    }
}


@Composable
fun SingleScreenApp(
    route: AppRoute,
    onNavigate: (AppRoute) -> Unit,
    onGoBack: () -> Unit,
    onSet: (AppRoute) -> Unit,
    onActivate: (TopLevelRoute) -> Unit
) {
//    val localUseNativeNavigation = staticCompositionLocalOf { false }
    CompositionLocalProvider(
        localUseNativeNavigation provides true)
    {
        Box(
            Modifier
                .fillMaxSize()
        ) {
            ScreenContent(
                route = route,
                onNavigate = onNavigate,
                onBack = onGoBack,
                onSet = onSet,
                onActivate = onActivate

            )
        }
    }
}

@Composable
internal fun ScreenContent(
    route: AppRoute,
    onNavigate: (AppRoute) -> Unit,
    onBack: () -> Unit,
    onSet: (AppRoute) -> Unit = {},
    onActivate: (TopLevelRoute) -> Unit,
) {
    when (route) {
        Home -> {
            HomeScreen(openDetails = { onNavigate(HomeDetails()) })
        }

        is HomeDetails -> {
            HomeDetailsScreen(onBack = onBack, openDetails = {})
        }

        Profile -> {
            ProfileScreen(openDetails = { onNavigate(ProfileDetails()) })
        }

        is ProfileDetails -> {
            ProfileDetailsScreen(
                onBack = onBack,
                openDetails = { onNavigate(ProfileDetailsNext()) })
        }

        Search -> {
            SearchScreen(openDetails = { onNavigate(SearchDetails()) })
        }

        is SearchDetails -> {
            SearchDetailsScreen(onBack = onBack, openDetails = {})
        }

        is ProfileDetailsNext -> {
            ProfileDetailsNextScreen(
                onBack = onBack,
                openDetails = { onNavigate(ProfileDetailsNextText()) }
            )
        }

        is ProfileDetailsNextText -> {
            ProfileDetailsNextTextScreen(
                onBack = onBack,
                openDetails = { onSet(SearchDetails()) }
            )
        }
    }
}




