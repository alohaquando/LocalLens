package com.oreomrone.locallens.ui.screens.details.map

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.components.layouts.ErrorOverlay
import com.oreomrone.locallens.ui.components.layouts.PostsMapLayout
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.PostViewModel

@Composable
fun DetailsPostsMap(
  backOnClick: () -> Unit = {},
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {}
) {
  val viewModel: DetailsPostsMapViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val postViewModel: PostViewModel = hiltViewModel()

  val context = LocalContext.current
  val packageManager = context.packageManager

  DetailsPostsMap(
    uiState = uiState,
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
    backOnClick = backOnClick
  )
}

@Composable
private fun DetailsPostsMap(
  uiState: DetailsPostsMapUiState = DetailsPostsMapUiState(),
  backOnClick: () -> Unit = {},
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {},
  navigateOnClick: (Double, Double, String) -> Unit = { _, _, _ -> },
  favoriteOnClick: suspend (String) -> Unit = {},
  deleteOnClick: suspend (String) -> Unit = {},
) {
  Crossfade(targetState = uiState.loadingState,
    label = "DetailsPostsMap Crossfade"
  ) {
    when (it) {
      LoadingStates.LOADING -> {
        LoadingOverlay()
      }

      LoadingStates.ERROR   -> {
        ErrorOverlay(backOnClick = backOnClick)
      }

      else -> {
        PostsMapLayout(
          postsClusterItems = uiState.postsClusterItems,
          mapStartingPosition = uiState.startingMapPosition,
          placeOnClick = placeOnClick,
          userOnClick = userOnClick,
          editOnClick = editOnClick,
          navigateOnClick = navigateOnClick,
          favoriteOnClick = favoriteOnClick,
          deleteOnClick = deleteOnClick,
          showBackButton = true,
          backOnClick = backOnClick,
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
private fun DetailsPostsMapPreview(

) {
  LocalLensTheme {
    DetailsPostsMap(
      DetailsPostsMapUiState()
    )
  }
}