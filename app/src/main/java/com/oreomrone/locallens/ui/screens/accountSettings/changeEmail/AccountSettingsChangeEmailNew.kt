package com.oreomrone.locallens.ui.screens.accountSettings.changeEmail

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
fun AccountSettingsChangeEmailNew(
  navigationButtonOnClick: () -> Unit = {},
//  navigateToVerifyEmail: () -> Unit = {},
) {
  val viewModel: AccountSettingsChangeEmailNewViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  AccountSettingsChangeEmailNew(
    uiState = uiState,
    navigationButtonOnClick = navigationButtonOnClick,
    onNewEmailChange = viewModel::onNewEmailChange,
    nextOnClick = viewModel::performSendVerifyEmail,
//    navigateToVerifyEmail = navigateToVerifyEmail,
  )
}

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalComposeUiApi::class
)
@Composable
private fun AccountSettingsChangeEmailNew(
  uiState: AccountSettingsChangeEmailNewUiState = AccountSettingsChangeEmailNewUiState(),
  navigationButtonOnClick: () -> Unit = {},
  onNewEmailChange: (String) -> Unit = {},
  nextOnClick: suspend () -> Unit = {},
//  navigateToVerifyEmail: () -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  val coroutineScope = rememberCoroutineScope()
  val keyboardController = LocalSoftwareKeyboardController.current

  var showAlertDialog by remember { mutableStateOf(false)}

  if (uiState.isLoading) {
    LoadingOverlay()
  }

//  if (uiState.isVerifyEmailSent) {
//    navigateToVerifyEmail()
//  }

  Scaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection)
      .imePadding(),
    topBar = {
      CenterAlignedTopAppBar(title = {
        Text("New email")
      },
        navigationIcon = {
          IconButton(onClick = { showAlertDialog = true }) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
            )
          }
        })
    },
    bottomBar = {
      BottomButtonBar(text = "Save",
        enabled = uiState.isNewEmailValid,
        onClick = {
          coroutineScope.launch {
            keyboardController?.hide()
            nextOnClick() // originally to send verification email. Should now just be to save new email
            navigationButtonOnClick() // navigate back once done
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

        OutlinedTextField(
          value = uiState.currentEmail,
          onValueChange = {},
          enabled = false,
          modifier = Modifier.fillMaxWidth(),
          label = { Text(text = "Current email") },
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        ErrorAwareOutlinedTextField(
          value = uiState.newEmail,
          onValueChange = { onNewEmailChange(it) },
          label = { Text(text = "Email") },
          isError = uiState.isNewEmailValid.not(),
          supportingText = { Text("The entered email address is in the wrong format") },
          maxLines = 1,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
      }
    }
  }

  if (showAlertDialog) {
    AlertDialog(title = {
      Text(text = "Cancel email change?")
    },
      text = {
        Text(
          text = "If you cancel now, your new email will not be saved."
        )
      },
      onDismissRequest = {
        showAlertDialog = false
      },
      confirmButton = {
        TextButton(onClick = {
          coroutineScope.launch {
            navigationButtonOnClick()
          }
        }) {
          Text("Cancel")
        }
      },
      dismissButton = {
        TextButton(onClick = {
          showAlertDialog = false
        }) {
          Text("Stay and continue")
        }
      })
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AccountSettingsChangeEmailNewPreview(

) {
  LocalLensTheme {
    AccountSettingsChangeEmailNew()
  }
}