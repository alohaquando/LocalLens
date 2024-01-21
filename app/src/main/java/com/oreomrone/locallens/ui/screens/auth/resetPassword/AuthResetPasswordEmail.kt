package com.oreomrone.locallens.ui.screens.auth.resetPassword

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.ui.components.BottomButtonBar
import com.oreomrone.locallens.ui.components.ErrorAwareOutlinedTextField
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import kotlinx.coroutines.launch


@Composable
fun AuthResetPasswordEmail(
  navigationButtonOnClick: () -> Unit = {},
  navigateToCheckEmail: () -> Unit = {},
) {
  val viewModel: AuthResetPasswordEmailViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  AuthResetPasswordEmail(
    uiState = uiState,
    navigationButtonOnClick = navigationButtonOnClick,
    onEmailChange = viewModel::onEmailChange,
    nextOnClick = viewModel::performSendVerifyEmail,
    navigateToCheckEmail = navigateToCheckEmail,
  )
}

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalComposeUiApi::class
)
@Composable
private fun AuthResetPasswordEmail(
  uiState: AuthResetPasswordEmailUiState = AuthResetPasswordEmailUiState(),
  navigationButtonOnClick: () -> Unit = {},
  onEmailChange: (String) -> Unit = {},
  nextOnClick: suspend () -> Unit = {},
  navigateToCheckEmail: () -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  val coroutineScope = rememberCoroutineScope()
  val keyboardController = LocalSoftwareKeyboardController.current

  if (uiState.isLoading) {
    LoadingOverlay()
  }

  if (uiState.isVerifyEmailSent) {
    navigateToCheckEmail()
  }

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).imePadding(),
    topBar = {
      CenterAlignedTopAppBar(title = {
        Text("Reset password")
      },
        navigationIcon = {
          IconButton(onClick = navigationButtonOnClick) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
            )
          }
        })
    },
    bottomBar = {
      BottomButtonBar(text = "Send password reset email",
        enabled = uiState.isEmailValid,
        onClick = {
          coroutineScope.launch {
            keyboardController?.hide()
            nextOnClick()
          }
        })
    },
    snackbarHost = {
      SnackbarHost(
        hostState = uiState.snackbarHostState
      )
    },
  ) { innerPadding ->
    Column(
      Modifier
        .padding(top = innerPadding.calculateTopPadding())
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .imePadding(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(
        space = 32.dp,
      ),
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(
          space = 8.dp,
          alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(modifier = Modifier.padding(top = 8.dp))

        Text(text = "Enter your email address to reset your password")

        ErrorAwareOutlinedTextField(
          value = uiState.email,
          onValueChange = { onEmailChange(it) },
          label = { Text(text = "Email") },
          isError = uiState.isEmailValid.not(),
          supportingText = { Text("The entered email address is in the wrong format") },
          maxLines = 1,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
      }
    }
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AuthResetPasswordEmailPreview(

) {
  LocalLensTheme {
    AuthResetPasswordEmail()
  }
}