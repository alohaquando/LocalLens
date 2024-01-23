package com.oreomrone.locallens.ui.screens.accountSettings


import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.ui.components.Alert
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import kotlinx.coroutines.launch

@Composable
fun AccountSettings(
  backOnClick: () -> Unit = {},
  changePasswordOnClick: () -> Unit = {},
  changeEmailOnClick: () -> Unit = {},
  supportOnClick: () -> Unit = {},
  manageOtherAccountOnClick: () -> Unit = {},
  ) {
  val viewModel: AccountSettingsViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  AccountSettings(
    uiState = uiState,
    backOnClick = backOnClick,
    changePasswordOnClick = changePasswordOnClick,
    changeEmailOnClick = changeEmailOnClick,
    supportOnClick = supportOnClick,
    signOutOnClick = viewModel::performSignOut,
    manageOtherAccountOnClick = manageOtherAccountOnClick,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountSettings(
  uiState: AccountSettingsUiState = AccountSettingsUiState(),
  backOnClick: () -> Unit = {},
  changePasswordOnClick: () -> Unit = {},
  changeEmailOnClick: () -> Unit = {},
  supportOnClick: () -> Unit = {},
  signOutOnClick: suspend () -> Unit = {},
  manageOtherAccountOnClick: () -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  val coroutineScope = rememberCoroutineScope()

  Scaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection)
      .imePadding(),
    topBar = {
      CenterAlignedTopAppBar(

        title = {
          Text(text = "Account settings")
        },
        navigationIcon = {
          IconButton(onClick = backOnClick) {
            Icon(
              imageVector = Icons.Filled.Close,
              contentDescription = "Close"
            )
          }
        },
        scrollBehavior = scrollBehavior,

        )
    },
  ) { innerPadding ->

    Column(
      Modifier
        .padding(top = innerPadding.calculateTopPadding())
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .imePadding()
    ) {

      if (uiState.isSuperUser) {
        Column {
          Alert(
            Icons.Rounded.VerifiedUser,
            "You are using a Super User account",
            "You can edit all posts and promote posts for free. You can also manage other accounts."
          )
          ListItem(
            headlineContent = { Text("Manage other accounts") },
            trailingContent = {
              Icon(
                Icons.Default.ArrowRight,
                contentDescription = "Next"
              )
            },
            modifier = Modifier.clickable(onClick = manageOtherAccountOnClick)
          )
          Divider()
        }
      }

      ListItem(
        headlineContent = { Text("Change password") },
        trailingContent = {
          Icon(
            Icons.Default.ArrowRight,
            contentDescription = "Next"
          )
        },
        modifier = Modifier.clickable(onClick = changePasswordOnClick)
      )
      ListItem(
        headlineContent = { Text("Change email") },
        trailingContent = {
          Icon(
            Icons.Default.ArrowRight,
            contentDescription = "Next"
          )
        },
        modifier = Modifier.clickable(onClick = changeEmailOnClick)
      )
      Divider(
        modifier = Modifier.padding(
          horizontal = 16.dp,
          vertical = 8.dp
        )
      )
      ListItem(
        headlineContent = { Text("Support") },
        trailingContent = {
          Icon(
            Icons.Default.ArrowRight,
            contentDescription = "Next"
          )
        },
        modifier = Modifier.clickable(onClick = supportOnClick)
      )
      Divider(
        modifier = Modifier.padding(
          horizontal = 16.dp,
          vertical = 8.dp
        )
      )
      ListItem(
        headlineContent = {
          Text(
            "Sign out",
            color = MaterialTheme.colorScheme.error
          )
        },
        modifier = Modifier.clickable(onClick = {
          coroutineScope.launch {
            signOutOnClick()
          }
        })
      )
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
private fun AccountSettingsPreview(

) {
  LocalLensTheme {
    AccountSettings()
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AccountSettingsSuperUserPreview(

) {
  LocalLensTheme {
    AccountSettings(uiState = AccountSettingsUiState(isSuperUser = true))
  }
}