package com.oreomrone.locallens.ui.screens.notifications

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.oreomrone.locallens.domain.AppNotification
import com.oreomrone.locallens.ui.components.Image
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Notifications(
  backOnClick: () -> Unit = {},
  notificationOnClick: (String) -> Unit = {},
) {
  val viewModel: NotificationsViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  // TODO: Ask permission
//  val context = LocalContext.current
//  val notificationPermissionState =
//    rememberPermissionState(
//      permission = Manifest.permission.POST_NOTIFICATIONS

  Notifications(
    uiState = uiState,
    backOnClick = backOnClick,
    notificationOnClick = notificationOnClick,
  )
}

@OptIn(
  ExperimentalMaterial3Api::class,
)
@Composable
private fun Notifications(
  uiState: NotificationsUiState = NotificationsUiState(),
  backOnClick: () -> Unit = {},
  notificationOnClick: (String) -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).imePadding(),
    topBar = {
      CenterAlignedTopAppBar(title = {
        Text(text = "Notifications")
      },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
          IconButton(onClick = { backOnClick() }) {
            Icon(
              imageVector = Icons.Rounded.ArrowBack,
              contentDescription = "Back",
            )
          }
        })
    },
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = innerPadding,
    ) {
      for (notification in uiState.notifications) {
        item {
          ListItem(overlineContent = {},
            leadingContent = {
              Image(
                model = notification.image,
                contentDescription = "Notification image",
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape),
              )
            },
            headlineContent = { Text(notification.content) },
            supportingContent = {
              Text(text = notification.timestamp)
            },
            modifier = Modifier.clickable {
              notificationOnClick(notification.destination)
            })
        }
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
private fun NotificationsPreview() {
  LocalLensTheme {
    Notifications(
      uiState = NotificationsUiState(
        notifications = listOf(
          AppNotification(
            id = "1",
            image = "https://picsum.photos/200/300",
            content = "This is a notification",
            timestamp = "2021-09-01T00:00:00.000Z",
            destination = "https://www.google.com",
          ),
          AppNotification(
            id = "1",
            image = "https://picsum.photos/200/300",
            content = "This is a notification",
            timestamp = "2021-09-01T00:00:00.000Z",
            destination = "https://www.google.com",
          ),
          AppNotification(
            id = "1",
            image = "https://picsum.photos/200/300",
            content = "This is a notification",
            timestamp = "2021-09-01T00:00:00.000Z",
            destination = "https://www.google.com",
          ),
        )
      )
    )
  }
}