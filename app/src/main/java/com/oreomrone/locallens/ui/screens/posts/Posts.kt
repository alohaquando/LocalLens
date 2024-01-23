package com.oreomrone.locallens.ui.screens.posts

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.ui.components.layouts.PostsMapLayout
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.PostViewModel

@Composable
fun Posts(
  notificationsOnClick: () -> Unit = {},
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {}
) {
  val viewModel: PostsViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val postViewModel: PostViewModel = hiltViewModel()

  val context = LocalContext.current
  val packageManager = context.packageManager

  Posts(
    uiState = uiState,
    notificationsOnClick = notificationsOnClick,
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
    refreshOnClick = viewModel::getPosts
  )
}

@Composable
private fun Posts(
  uiState: PostsUiState = PostsUiState(),
  notificationsOnClick: () -> Unit = {},
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {},
  navigateOnClick: (Double, Double, String) -> Unit = { _, _, _ -> },
  favoriteOnClick: suspend (String) -> Unit = {},
  deleteOnClick: suspend (String) -> Unit = {},
  refreshOnClick: suspend () -> Unit = {}
) {
  PostsMapLayout(
    posts = uiState.posts,
    postsClusterItems = uiState.postsClusterItems,
    mapStartingPosition = uiState.startingMapPosition,
    notificationsOnClick = notificationsOnClick,
    placeOnClick = placeOnClick,
    userOnClick = userOnClick,
    editOnClick = editOnClick,
    navigateOnClick = navigateOnClick,
    favoriteOnClick = favoriteOnClick,
    deleteOnClick = deleteOnClick,
    refreshOnClick = refreshOnClick
  )
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PostsPreview(

) {
  LocalLensTheme {
    Posts()
  }
}