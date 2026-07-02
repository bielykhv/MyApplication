package com.example.myapplication.app_routes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute{
    val title: String? get() = null
}


@Serializable
sealed interface TopLevelRoute : AppRoute

@Serializable
@SerialName("Home")
data object Home : AppRoute, TopLevelRoute

@Serializable
@SerialName("HomeDetails")
data class HomeDetails(override val title: String? = "Home details") : AppRoute

@Serializable
@SerialName("Search")
data object Search : AppRoute, TopLevelRoute

@Serializable
@SerialName("SearchDetails")
data class SearchDetails(override val title: String = "Search details") : AppRoute {

}

@Serializable
@SerialName("Profile")
data object Profile : AppRoute, TopLevelRoute

@Serializable
@SerialName("ProfileDetails")
data class ProfileDetails(override val title: String? = "Profile details") : AppRoute
@Serializable
@SerialName("ProfileDetailsNext")
data class ProfileDetailsNext(override val title: String? = "Profile details next") : AppRoute
@Serializable
@SerialName("ProfileDetailsNextText")
data class ProfileDetailsNextText(override val title: String? = "Profile details next text") : AppRoute

