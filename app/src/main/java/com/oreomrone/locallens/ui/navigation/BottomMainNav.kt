package com.oreomrone.locallens.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomMainNavItem(
  val destination: String,
  val icon: ImageVector,
  val activeIcon: ImageVector,
)

val AppBottomMainNavItems = listOf(
  BottomMainNavItem(
    destination = AppNavDests.Posts.name,
    icon = Icons.Outlined.Map,
    activeIcon = Icons.Rounded.Map,
  ),
  BottomMainNavItem(
    destination = AppNavDests.MessagesList.name,
    icon = Icons.Outlined.Message,
    activeIcon = Icons.Rounded.Message,
  ),
  BottomMainNavItem(
    destination = AppNavDests.Discover.name,
    icon = Icons.Outlined.Search,
    activeIcon = Icons.Rounded.Search,
  ),
  BottomMainNavItem(
    destination = AppNavDests.Me.name,
    icon = Icons.Outlined.AccountCircle,
    activeIcon = Icons.Rounded.AccountCircle,
  ),
)

val BottomMainNavVisibleDestinations = listOf(
  AppNavDests.Posts.name,
  AppNavDests.MessagesList.name,
  AppNavDests.Discover.name,
  AppNavDests.DiscoverSearch.name,
  AppNavDests.Me.name,
  AppNavDests.DetailsPerson.name,
  AppNavDests.DetailsPlace.name,
//  AppNavDests.AccountSettings.name,
)
