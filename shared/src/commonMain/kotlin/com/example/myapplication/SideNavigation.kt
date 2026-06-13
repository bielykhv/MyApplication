package com.example.myapplication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SideNavigation(
    currentRoute: TopLevelRoute?,
    destinations: List<MainNavDestination<TopLevelRoute>>,
    onSelectRoute: (TopLevelRoute) -> Unit,
    expanded: Boolean,
) {
    val currentDestination = destinations.find { it.route == currentRoute }

    Row {
        MainNavigationRail(
            currentDestination = currentDestination,
            destinations = destinations,
            onSelect = { selectedDestination ->
                onSelectRoute(selectedDestination.route)
            },
            expanded = expanded,
//            modifier = Modifier.padding(topInsetPadding()),
        )
    }
}
@Composable
fun <T : Any> MainNavigationRail(
    currentDestination: MainNavDestination<T>?,
    destinations: List<MainNavDestination<T>>,
    onSelect: (MainNavDestination<T>) -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val width by animateDpAsState(
        if (expanded) 220.dp else 140.dp,
        animationSpec = spring(
            visibilityThreshold = Dp.VisibilityThreshold,
            stiffness = Spring.StiffnessLow
        ),
    )
    Column(
        modifier = modifier
            .width(width)
            .padding(12.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        destinations.forEach { destination ->
            NavRailMenuItem(
                iconResource = destination.icon,
                iconFilledResource = destination.iconSelected,
                label = destination.label,
                selected = destination == currentDestination,
                expanded = expanded,
                onClick = { onSelect(destination) },
            )
        }
    }
}
@Composable
private fun NavRailMenuItem(
    iconResource: DrawableResource,
    iconFilledResource: DrawableResource,
    label: String,
    selected: Boolean,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {


    val itemModifier = modifier
        .fillMaxWidth()
        .selectable(
            selected = selected,
            enabled = true,
            role = Role.Tab,
            onClick = onClick,
        )
        .padding(12.dp)

    val arrangement = Arrangement.spacedBy(8.dp)

    SharedTransitionLayout(
        modifier = itemModifier,
    ) {
        AnimatedContent(expanded, modifier = Modifier.fillMaxWidth()) { isExpanded ->
            val itemContent = @Composable {
                Icon(
                    modifier = Modifier.size(28.dp)
                        .sharedElement(
                            rememberSharedContentState(key = "icon"),
                            animatedVisibilityScope = this@AnimatedContent,
                        ),
                    painter = painterResource(if (selected) iconFilledResource else iconResource),
                    contentDescription = null,
//                    tint = contentColor,
                )
                Text(
                    modifier = Modifier.sharedElement(
                        rememberSharedContentState(key = "text"),
                        animatedVisibilityScope = this@AnimatedContent,
                    ),
                    text = label,
//                    color = contentColor,
                )
            }

            if (isExpanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = arrangement,
                ) {
                    itemContent()
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = arrangement,
                ) {
                    itemContent()
                }
            }
        }
    }
}

