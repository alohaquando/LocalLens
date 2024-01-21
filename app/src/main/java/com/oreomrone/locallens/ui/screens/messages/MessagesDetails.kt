package com.oreomrone.locallens.ui.screens.messages

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.components.BottomTextMessageBar
import com.oreomrone.locallens.ui.components.Image
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.components.layouts.ErrorOverlay
import com.oreomrone.locallens.ui.components.layouts.Message
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.SampleData
import kotlinx.coroutines.launch

@Composable
fun MessagesDetails(
  backOnClick: () -> Unit = {},
) {
  val viewModel: MessagesDetailsViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  MessagesDetails(
    uiState = uiState,
    backOnClick = backOnClick,
    onDraftMessageChange = viewModel::onDraftMessageChange,
    messageSendOnClick = viewModel::performSendMessage
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MessagesDetails(
  uiState: MessagesDetailsUiState = MessagesDetailsUiState(),
  backOnClick: () -> Unit = {},
  onDraftMessageChange: (String) -> Unit = {},
  messageSendOnClick: suspend () -> Unit = {},
) {
  val coroutineScope = rememberCoroutineScope()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(modifier = Modifier
    .nestedScroll(scrollBehavior.nestedScrollConnection)
    .imePadding(),
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          if (uiState.recipient !== null) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Image(
                model = uiState.recipient.image,
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape),
              )
              Text(
                text = "@${uiState.recipient.username}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          } else {
            Text(text = "Message")
          }
        },
        navigationIcon = {
          IconButton(onClick = { backOnClick() }) {
            Icon(
              imageVector = Icons.Rounded.ArrowBack,
              contentDescription = "Back",
            )
          }
        },
        scrollBehavior = scrollBehavior
      )
    },
    bottomBar = {
      if (uiState.thread !== null) {
        BottomTextMessageBar(value = uiState.draftMessage,
          onValueChange = { message ->
            onDraftMessageChange(message)
          },
          buttonOnClick = {
            coroutineScope.launch {
              messageSendOnClick()
            }
          })
      }
    }) { innerPadding ->
    Crossfade(
      targetState = uiState.loadingState,
      label = "MessageDetails Crossfade"
    ) {
      when (it) {
        LoadingStates.LOADING -> {
          LoadingOverlay()
        }

        LoadingStates.ERROR   -> {
          ErrorOverlay(
            showBackButton = false
          )
        }

        else                  -> {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 16.dp),
            contentPadding = innerPadding,
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(
              8.dp,
              alignment = Alignment.Bottom
            )
          ) {
            for (message in uiState.thread!!.messages) {
              item {
                Message(
                  text = message.content,
                  byCurrentUser = message.sender == uiState.currentUser,
                  timeStamp = message.timestamp
                )
              }
            }
          }
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
private fun MessagesDetailsPreview(

) {
  LocalLensTheme {
    MessagesDetails(
      uiState = MessagesDetailsUiState(
        thread = SampleData.sampleThread,
        currentUser = SampleData.sampleThread.participants.first,
        recipient = SampleData.sampleThread.participants.second,
        loadingState = LoadingStates.SUCCESS
      ),
    )
  }
}