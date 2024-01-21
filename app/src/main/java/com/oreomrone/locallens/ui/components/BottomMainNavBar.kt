package com.oreomrone.locallens.ui.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.oreomrone.locallens.ui.navigation.AppBottomMainNavItems
import com.oreomrone.locallens.ui.navigation.AppNavDests
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun BottomMainNavBar(
  currentDestination: String? = null,
  destinationOnClick: (String) -> Unit = { }
) {
  BottomAppBar(actions = {
    for (item in AppBottomMainNavItems) {
      if (currentDestination !== item.destination) {
        IconButton(onClick = { destinationOnClick(item.destination) }) {
          Icon(
            item.icon,
            "Localized description"
          )
        }
      } else {
        FilledTonalIconButton(onClick = { destinationOnClick(item.destination) }) {
          Icon(
            item.activeIcon,
            "Localized description"
          )
        }
      }
    }
  },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { destinationOnClick(AppNavDests.NewPostDetails.name) },
        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
      ) {
        Icon(
          Icons.Filled.Add,
          "New post"
        )
      }
    })
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BottomMainNavBarPreview(

) {
  LocalLensTheme {
    Scaffold(bottomBar = { BottomMainNavBar() }) {

    }
  }
}