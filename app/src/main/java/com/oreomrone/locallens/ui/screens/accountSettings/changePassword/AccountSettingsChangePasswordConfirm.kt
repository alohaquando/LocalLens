package com.oreomrone.locallens.ui.screens.accountSettings.changePassword

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
fun AccountSettingsChangePasswordConfirm(
  navigateToNewPassword: () -> Unit = {},
) {
  val viewModel: AccountSettingsChangePasswordConfirmViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  AccountSettingsChangePasswordConfirm(
    uiState = uiState,
    onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
    nextOnClick = viewModel::performValidateCurrentPassword,
    navigateToNewPassword = navigateToNewPassword,
  )
}

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalComposeUiApi::class
)
@Composable
private fun AccountSettingsChangePasswordConfirm(
  uiState: AccountSettingsChangePasswordConfirmUiState = AccountSettingsChangePasswordConfirmUiState(),
  navigationButtonOnClick: () -> Unit = {},
  onCurrentPasswordChange: (String) -> Unit = {},
  nextOnClick: suspend () -> Unit = {},
  navigateToNewPassword: () -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  val coroutineScope = rememberCoroutineScope()
  val keyboardController = LocalSoftwareKeyboardController.current

  if (uiState.isLoading) {
    LoadingOverlay()
  }

  if (uiState.isCurrentPasswordValid) {
    navigateToNewPassword()
  }

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).imePadding(),
    topBar = {
      CenterAlignedTopAppBar(title = {
        Text("Confirm current password")
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
      BottomButtonBar(text = "Next",
        enabled = uiState.currentPassword.isNotBlank(),
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
          space = 12.dp,
          alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        ErrorAwareOutlinedTextField(
          modifier = Modifier.padding(top = 12.dp),
          value = uiState.currentPassword,
          onValueChange = { onCurrentPasswordChange(it) },
          label = { Text("Confirm current password") },
          visualTransformation = PasswordVisualTransformation(),
          isError = uiState.isCurrentPasswordValid.not(),
          supportingText = { Text("The entered password does not match the current password") },
          maxLines = 1,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
private fun AccountSettingsChangePasswordConfirmPreview(

) {
  LocalLensTheme {
    AccountSettingsChangePasswordConfirm()
  }
}