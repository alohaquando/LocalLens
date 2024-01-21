package com.oreomrone.locallens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.components.BottomMainNavBar
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.navigation.AppNavDests
import com.oreomrone.locallens.ui.navigation.AppNavHost
import com.oreomrone.locallens.ui.navigation.BottomMainNavVisibleDestinations
import com.oreomrone.locallens.ui.tests.AuthGoogleTest
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.AuthViewModel
import com.oreomrone.locallens.ui.utils.TestViewModel
import com.oreomrone.locallens.ui.utils.conditional
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @Inject
  lateinit var supabaseClient: SupabaseClient

  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    supabaseClient.handleDeeplinks(intent = intent)

    setContent {
      val appNavController = rememberNavController()
      val currentDestination =
        appNavController.currentBackStackEntryAsState().value?.destination?.route
      val showBottomMainNavBar = currentDestination in BottomMainNavVisibleDestinations

      val authViewModel: AuthViewModel = hiltViewModel()
      val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()

      val testViewModel: TestViewModel= hiltViewModel()
      val testUiState by testViewModel.uiState.collectAsStateWithLifecycle()

      LocalLensTheme {
        Scaffold(
          // App main nav bar
          bottomBar = {
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
            // App main nav host
            Crossfade(targetState = authUiState.loadingStates,
              label = "MainActivity Crossfade"
            ) {
              when (it) {
                LoadingStates.LOADING, LoadingStates.ERROR -> {
                  LoadingOverlay()
                }
                LoadingStates.SUCCESS -> {
                  AppNavHost(
                    navController = appNavController,
                    startDestination = authUiState.startingDestination!!
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}