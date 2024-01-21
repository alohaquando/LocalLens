package com.oreomrone.locallens.ui.screens.messages

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.ui.components.Image
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.SampleData
import com.oreomrone.locallens.ui.utils.getRecipientFromThreadParticipantsPair

@Composable
fun MessagesList(
  threadOnClick: (String) -> Unit = {},
) {
  val viewModel: MessagesListViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  MessagesList(
    uiState = uiState,
    threadOnClick = threadOnClick,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MessagesList(
  uiState: MessagesListUiState = MessagesListUiState(),
  threadOnClick: (String) -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).imePadding(),
    topBar = {
      CenterAlignedTopAppBar(title = {
        Text(text = "Messages")
      },
        scrollBehavior = scrollBehavior,
        )
    },
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = innerPadding,
    ) {
      for (thread in uiState.threads) {
        item {
          val recipient = getRecipientFromThreadParticipantsPair(
            thread.participants,
            uiState.currentUser!!
          )
          ListItem(overlineContent = {},
            leadingContent = {
              Image(
                clickable = false,
                model = recipient.image,
                contentDescription = "User image",
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape),
              )
            },
            trailingContent = {
              Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Text(text = thread.lastUpdated)
                if (thread.hasUnreadMessage) {
                  Icon(
                    imageVector = Icons.Rounded.Circle,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                  )
                }
              }
            },
            headlineContent = { Text("@${recipient.username}") },
            supportingContent = {
              Text(text = thread.messages.last().content)
            },
            modifier = Modifier.clickable {
              threadOnClick(thread.id)
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
private fun MessagesListPreview(

) {
  LocalLensTheme {
    MessagesList(
      uiState = MessagesListUiState(
        threads = listOf(
          SampleData.sampleThread,
          SampleData.sampleThread1,
          SampleData.sampleThread,
          SampleData.sampleThread1,
          SampleData.sampleThread,
          SampleData.sampleThread1,
          SampleData.sampleThread,
          SampleData.sampleThread1,
        )
      )
    )
  }
}