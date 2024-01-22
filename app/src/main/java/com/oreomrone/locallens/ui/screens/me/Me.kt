package com.oreomrone.locallens.ui.screens.me

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
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
import com.oreomrone.locallens.ui.components.Post
import com.oreomrone.locallens.ui.components.StatItemButton
import com.oreomrone.locallens.ui.components.layouts.DetailsLayout
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.PostViewModel
import com.oreomrone.locallens.ui.utils.SampleData
import kotlinx.coroutines.launch

@Composable
fun Me(
  backOnClick: () -> Unit = {},
  editProfileOnClick: () -> Unit = {},
  accountSettingsOnClick: () -> Unit = {},
  placesOnClick: (String) -> Unit = {},
  followersOnClick: (String) -> Unit = {},
  followingOnClick: (String) -> Unit = {},
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {}
) {
  val viewModel: MeViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val postViewModel: PostViewModel = hiltViewModel()

  val context = LocalContext.current
  val packageManager = context.packageManager

  Me(
    uiState = uiState,
    backOnClick = backOnClick,
    editProfileOnClick = editProfileOnClick,
    appSettingsOnClick = accountSettingsOnClick,
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
  )
}

@OptIn(
  ExperimentalLayoutApi::class
)
@Composable
private fun Me(
  uiState: MeUiState = MeUiState(),
  backOnClick: () -> Unit = {},
  editProfileOnClick: () -> Unit = {},
  appSettingsOnClick: () -> Unit = {},
  placesOnClick: (String) -> Unit = {},
  followersOnClick: (String) -> Unit = {},
  followingOnClick: (String) -> Unit = {},
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {},
  navigateOnClick: (Double, Double, String) -> Unit = { _, _, _ -> },
  favoriteOnClick: suspend (String) -> Unit = {},
  deleteOnClick: suspend (String) -> Unit = {},
) {
  val coroutineScope = rememberCoroutineScope()

  DetailsLayout(title = if (uiState.user != null) "@${uiState.user.username}" else "",
    subtitle = uiState.user?.name ?: "",
    image = uiState.user?.image ?: "",
    ternaryText = uiState.user?.bio ?: "",
    backOnClick = backOnClick,
    isLoading = uiState.user == null,
    isPrivate = uiState.user?.isPrivate ?: false,
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
          onClick = editProfileOnClick,
          modifier = Modifier.weight(1f)
        ) {
          Text(text = "Edit Profile")
        }
        OutlinedIconButton(onClick = appSettingsOnClick) {
          Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = "",
          )
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
    }
  ) {
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
          showMenuButton = true,
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

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun MePreview(
) {
  LocalLensTheme {
    Me(uiState = MeUiState(user = SampleData.sampleUser))
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun MeLoadingPreview(
) {
  LocalLensTheme {
    Me(uiState = MeUiState(user = null))
  }
}