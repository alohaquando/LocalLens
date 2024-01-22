package com.oreomrone.locallens.ui.screens.details

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.components.Post
import com.oreomrone.locallens.ui.components.StatItemButton
import com.oreomrone.locallens.ui.components.layouts.DetailsLayout
import com.oreomrone.locallens.ui.components.layouts.ErrorOverlay
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.PostViewModel
import com.oreomrone.locallens.ui.utils.SampleData
import kotlinx.coroutines.launch

@Composable
fun DetailsPerson(
  backOnClick: () -> Unit = {},
  placesOnClick: (String) -> Unit = {},
  followersOnClick: (String) -> Unit = {},
  followingOnClick: (String) -> Unit = {},
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {},
  messageOnClick: () -> Unit = {},
) {
  val viewModel: DetailsPersonViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val postViewModel: PostViewModel = hiltViewModel()

  val context = LocalContext.current
  val packageManager = context.packageManager

  DetailsPerson(
    uiState = uiState,
    backOnClick = backOnClick,
    followOnClick = {},
    placesOnClick = placesOnClick,
    followersOnClick = followersOnClick,
    followingOnClick = followingOnClick,
    placeOnClick = placeOnClick,
    userOnClick = userOnClick,
    editOnClick = editOnClick,
    navigateOnClick = { lat: Double, long: Double, name: String ->
      postViewModel.performNavigate(
        lat,
        long,
        name,
        context,
        packageManager
      )
    },
    favoriteOnClick = postViewModel::performFavoritePost,
    deleteOnClick = postViewModel::performDeletePost,
    messageOnClick = messageOnClick,
  )
}

@OptIn(
  ExperimentalLayoutApi::class
)
@Composable
private fun DetailsPerson(
  uiState: DetailsPersonUiState = DetailsPersonUiState(),
  backOnClick: () -> Unit = {},
  followOnClick: () -> Unit = {},
  placesOnClick: (String) -> Unit = {},
  followersOnClick: (String) -> Unit = {},
  followingOnClick: (String) -> Unit = {},
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {},
  messageOnClick: () -> Unit = {},
  navigateOnClick: (Double, Double, String) -> Unit = { _, _, _ -> },
  favoriteOnClick: suspend (String) -> Unit = {},
  deleteOnClick: suspend (String) -> Unit = {},
) {
  val coroutineScope = rememberCoroutineScope()

  Crossfade(
    targetState = uiState.loadingStates,
    label = "DetailsPerson Crossfade"
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

      else                  -> {
        DetailsLayout(title = if (uiState.user != null) "@${uiState.user.username}" else "",
          subtitle = uiState.user?.name ?: "",
          image = uiState.user?.image ?: "",
          ternaryText = uiState.user?.bio ?: "",
          showBackButton = true,
          backOnClick = backOnClick,
          isLoading = uiState.user == null,
          buttonsRowContent = {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
              horizontalArrangement = Arrangement.spacedBy(
                8.dp,
              ),
            ) {
              FilledTonalButton(
                onClick = followOnClick,
                modifier = Modifier.weight(1f)
              ) {
                // TODO: Dynamic this
                Text(text = "Follow")
              }
              OutlinedButton(onClick = { messageOnClick() }) {
                Text(text = "Message")
              }
            }
          },
          statsRowContent = {
            FlowRow(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
              horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.CenterHorizontally
              ),
              verticalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.CenterVertically
              )
            ) {
              StatItemButton(
                modifier = Modifier.weight(1f),
                onClick = { },
                value = uiState.user?.posts?.size.toString(),
                label = "Posts",
              )

              StatItemButton(
                modifier = Modifier.weight(1f),
                onClick = ({ placesOnClick(uiState.user?.id.toString()) }),
                value = uiState.user?.places?.size.toString(),
                label = "Places",
              )

              StatItemButton(
                modifier = Modifier.weight(1f),
                onClick = { followersOnClick(uiState.user?.id.toString()) },
                value = uiState.user?.followers?.size.toString(),
                label = "Followers",
              )

              StatItemButton(
                modifier = Modifier.weight(1f),
                onClick = { followingOnClick(uiState.user?.id.toString()) },
                value = uiState.user?.followings?.size.toString(),
                label = "Following",
              )
            }
          }) {
          if (uiState.user != null) {
            for (post in uiState.user.posts) {
              Post(
                place = post.place.name,
                address = post.place.address,
                caption = post.caption,
                username = uiState.user.username,
                date = post.timestamp,
                favorites = post.favorites.size,
                postImageModel = post.image,
                userImageModel = uiState.user.image,
                isFavorite = false,
                showDivider = true,
                showUser = false,
                showMenuButton = false, // TODO: allow for superUser
                navigateOnClick = {
                  navigateOnClick(
                    post.place.latitude,
                    post.place.longitude,
                    post.place.name
                  )
                },
                favoriteOnClick = {
                  coroutineScope.launch {
                    favoriteOnClick(post.id)
                  }
                },
                placeOnClick = { placeOnClick(post.place.id) },
                userOnClick = { userOnClick(uiState.user.id) },
                editOnClick = { editOnClick(post.id) },
                deleteOnClick = {
                  coroutineScope.launch {
                    deleteOnClick(post.id)
                  }
                },
              )
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
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun DetailsPersonPreview(
) {
  LocalLensTheme {
    DetailsPerson(
      uiState = DetailsPersonUiState(
        user = SampleData.sampleUser1,
        loadingStates = LoadingStates.IDLE
      )
    )
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun DetailsPersonLoadingPreview(
) {
  LocalLensTheme {
    DetailsPerson(uiState = DetailsPersonUiState(user = null))
  }
}