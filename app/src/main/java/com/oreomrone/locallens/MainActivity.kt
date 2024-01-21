package com.oreomrone.locallens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oreomrone.locallens.ui.components.BottomMainNavBar
import com.oreomrone.locallens.ui.navigation.AppNavDests
import com.oreomrone.locallens.ui.navigation.AppNavHost
import com.oreomrone.locallens.ui.navigation.BottomMainNavVisibleDestinations
import com.oreomrone.locallens.ui.tests.CameraTest
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.conditional
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val appNavController = rememberNavController()
      val currentDestination =
        appNavController.currentBackStackEntryAsState().value?.destination?.route
      val showBottomMainNavBar = currentDestination in BottomMainNavVisibleDestinations

      LocalLensTheme {
        Scaffold(bottomBar = {
          AnimatedVisibility(
            visible = showBottomMainNavBar,
            enter = fadeIn(),
            exit = fadeOut()
          ) {
            BottomMainNavBar(currentDestination = currentDestination,
              destinationOnClick = {
                appNavController.navigate(it) {
                  popUpTo(appNavController.graph.findStartDestination().id) {
                    saveState = true
                  }
                  launchSingleTop = true
                  restoreState = true
                }
              })
          }
        }) {
          Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
          ) {
            AppNavHost(modifier = Modifier.conditional(showBottomMainNavBar) {
              padding(bottom = it.calculateBottomPadding())
            },
              navController = appNavController,
              startDestination = AppNavDests.Posts.name) // Change entry here
          }
        }
      }
    }
  }
}