package com.example.myapplication.app_routes

import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.DrawableResource

val bottomNavDestinations: List<MainNavDestination<TopLevelRoute>> = listOf(
    MainNavDestination(
        label = "Home",
        route = Home,
        icon = Res.drawable.compose_multiplatform
    ),
    MainNavDestination(
        label = "Search",
        route = Search,
        icon = Res.drawable.compose_multiplatform

    ),
    MainNavDestination(
        label = "Profile",
        route = Profile,
        icon = Res.drawable.compose_multiplatform
    )
)

data class MainNavDestination<T : Any>(
    val label: String,
    val icon: DrawableResource,
    val route: T,
    val iconSelected: DrawableResource = icon,
)