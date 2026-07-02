package com.example.myapplication.app_routes

import com.example.myapplication.NavState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter

class Navigator(
    val state: NavState,
    val topLevelBackEnabled: Boolean,
) {
    private val _tabReselections = MutableSharedFlow<TopLevelRoute>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    fun tabReselections(forRoute: TopLevelRoute): Flow<TopLevelRoute> =
        _tabReselections.filter { it == forRoute }

    fun goBack() {
        val currentBackstack = state.currentBackstack

        if (state.topLevelRoute == null) {
            // We're using the default stack, remove an entry if possible
            if (currentBackstack.size > 1) {
                currentBackstack.removeLastOrNull()
            }
            return
        }
        if (currentBackstack.size == 1 && state.topLevelRoute != state.primaryTopLevelRoute) {
            // Can't go further up on current backstack, but we're not on the primary route
            if (topLevelBackEnabled) {
                // Go back to the primary top-level route if enabled
                state.topLevelRoute = state.primaryTopLevelRoute
            }
        } else if (currentBackstack.size > 1) {
            currentBackstack.removeLastOrNull()
        }
    }

    fun add(route: AppRoute) {
        if (route is TopLevelRoute) {
            activate(route)
        } else {
            state.currentBackstack.add(route)
        }
    }

    fun set(route: AppRoute) {
        state.currentBackstack.clear()
        add(route)
    }

    fun activate(route: TopLevelRoute, withReselection: Boolean = true) {
        if (withReselection && route == state.topLevelRoute) {
            val currentBackstack = state.currentBackstack

            // Reselected the current top-level route, clear to root
            if (currentBackstack.size > 1) {
                currentBackstack.removeRange(1, currentBackstack.size)
            } else {
                // Already at root, signal reselection for scroll-to-top
                _tabReselections.tryEmit(route)
            }
            return
        }
        state.topLevelRoute = route
    }

    // -------------------------
    // 🔥 CROSS TAB NAVIGATION
    // -------------------------
    fun crossTab(target: AppRoute) {
        val targetTab = target.toTopLevel()

        // 1. очистить текущий таб до root
        val currentStack = state.currentBackstack
        if (currentStack.isNotEmpty()) {
            currentStack.removeRange(1, currentStack.size)
        }

        // 2. переключить таб
        state.topLevelRoute = targetTab

        // 3. сбросить новый стек
        state.currentBackstack.clear()

        // 4. добавить root нового таба
        state.currentBackstack.add(targetTab)

        // 5. построить путь до нужного экрана
        buildPath(target)
    }

    // -------------------------
    // ROUTE → TAB MAPPING
    // -------------------------
    private fun AppRoute.toTopLevel(): TopLevelRoute {
        return when (this) {
            is Home, is HomeDetails -> Home
            is Profile, is ProfileDetails, is ProfileDetailsNext, is ProfileDetailsNextText -> Profile
            is Search, is SearchDetails -> Search
        }
    }

    // -------------------------
    // PATH BUILDER
    // -------------------------
    private fun buildPath(route: AppRoute) {
        when (route) {

            is HomeDetails -> {
                state.currentBackstack.add(HomeDetails())
            }

            is SearchDetails -> {
                state.currentBackstack.add(SearchDetails())
            }

            is ProfileDetails -> {
                state.currentBackstack.add(ProfileDetails())
            }

            is ProfileDetailsNext -> {
                state.currentBackstack.add(ProfileDetails())
                state.currentBackstack.add(ProfileDetailsNext())
            }

            is ProfileDetailsNextText -> {
                state.currentBackstack.add(ProfileDetails())
                state.currentBackstack.add(ProfileDetailsNext())
                state.currentBackstack.add(ProfileDetailsNextText())
            }

            else -> Unit
        }
    }
}