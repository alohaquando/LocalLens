package com.oreomrone.locallens.ui.screens.details.follows

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.components.Image
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.components.layouts.ErrorOverlay
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.SampleData

@Composable
fun DetailsFollowers(
  backOnClick: () -> Unit = {},
  userOnClick: (String) -> Unit = {},
) {
  val viewModel: DetailsFollowersViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  DetailsFollowers(
    uiState = uiState,
    backOnClick = backOnClick,
    userOnClick = userOnClick
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsFollowers(
  uiState: DetailsFollowersUiState = DetailsFollowersUiState(),
  backOnClick: () -> Unit = {},
  userOnClick: (String) -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Crossfade(
    targetState = uiState.loadingState,
    label = "DetailsFollowers Crossfade"
  ) {
    when (it) {
      LoadingStates.LOADING -> {
        LoadingOverlay()
      }

      LoadingStates.ERROR   -> {
        ErrorOverlay(
          backOnClick = backOnClick
        )
      }

      LoadingStates.SUCCESS -> {
        Scaffold(
          modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .imePadding(),
          topBar = {
            CenterAlignedTopAppBar(title = {
              Text(text = "Followers")
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
          if (uiState.followers.isEmpty()) {
            Text(
              text = "No followers yet",
              textAlign = TextAlign.Center,
              modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(
                  horizontal = 16.dp,
                  vertical = 24.dp
                )
                .alpha(0.7f)
            )
          } else {
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              contentPadding = innerPadding,
            ) {
              for (user in uiState.followers) {
                item {
                  ListItem(overlineContent = {},
                    leadingContent = {
                      Image(
                        model = user.image,
                        contentDescription = "User image",
                        modifier = Modifier
                          .size(40.dp)
                          .clip(CircleShape),
                      )
                    },
                    headlineContent = { Text("@${user.username}") },
                    supportingContent = {
                      Text(text = user.name)
                    },
                    modifier = Modifier.clickable {
                      userOnClick(user.id)
                    })
                }
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
private fun DetailsFollowersPreview(

) {
  LocalLensTheme {
    DetailsFollowers(
      DetailsFollowersUiState(
        followers = listOf(
          SampleData.sampleUser,
          SampleData.sampleUser1,
          SampleData.sampleUser2,
          SampleData.sampleSuperUser
        )
      )
    )
  }
}