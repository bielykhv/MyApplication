package com.example.myapplication

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.compose_multiplatform

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