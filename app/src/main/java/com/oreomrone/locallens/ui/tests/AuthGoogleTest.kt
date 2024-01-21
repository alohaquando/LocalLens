package com.oreomrone.locallens.ui.tests

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import io.github.jan.supabase.gotrue.SignOutScope
import io.github.jan.supabase.gotrue.providers.Google
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AuthGoogleTest(
  viewModel: CameraTestViewModel = hiltViewModel()
) {
  val couroutineScope = rememberCoroutineScope()

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val action =
    viewModel.supabaseClient.composeAuth.rememberSignInWithGoogle(onResult = { result -> //optional error handling
      when (result) {
        is NativeSignInResult.Success      -> {
          Log.d(
            "QuanTag",
            "Success"
          )
        }

        is NativeSignInResult.ClosedByUser -> {
          Log.d(
            "QuanTag",
            "ClosedByUser"
          )

        }

        is NativeSignInResult.Error        -> {
          couroutineScope.launch {
            viewModel.auth.signInWith(Google)
          }

          Log.d(
            "QuanTag",
            "Error"
          )

        }

        is NativeSignInResult.NetworkError -> {
          Log.d(
            "QuanTag",
            "NetworkError"
          )

        }
      }
    },
      fallback = { // optional: add custom error handling, not required by default
        viewModel.auth.signInWith(Google)
      })



  Column(
    Modifier
      .padding(WindowInsets.systemBars.asPaddingValues())
      .verticalScroll(rememberScrollState())
  ) {
    Text(text = uiState.sessionText)

    Button(onClick = { action.startFlow() }) {
      Text("Google Login")
    }
    Button(onClick = {
      couroutineScope.launch {
        viewModel.auth.signOut(SignOutScope.LOCAL)
      }
    }) {
      Text(text = "Sign out now")
    }
  }
}