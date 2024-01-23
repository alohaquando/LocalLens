package com.oreomrone.locallens.ui.screens.details

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun DetailsPlace(
  backOnClick: () -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {}
) {
  val viewModel: DetailsPlaceViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val postViewModel: PostViewModel = hiltViewModel()

  val context = LocalContext.current
  val packageManager = context.packageManager

  DetailsPlace(
    uiState = uiState,
    backOnClick = backOnClick,
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
private fun DetailsPlace(
  uiState: DetailsPlaceUiState = DetailsPlaceUiState(),
  backOnClick: () -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {},
  navigateOnClick: (Double, Double, String) -> Unit = { _, _, _ -> },
  favoriteOnClick: suspend (String) -> Unit = {},
  deleteOnClick: suspend (String) -> Unit = {},
) {
  val coroutineScope = rememberCoroutineScope()

  Crossfade(targetState = uiState.loadingStates,
    label = "DetailsPlace Crossfade"
  ) {
    when (it) {
      LoadingStates.LOADING -> {
        LoadingOverlay()
      }

      LoadingStates.ERROR   -> {
        ErrorOverlay(
          backOnClick = backOnClick,
        )
      }

      else -> {
        DetailsLayout(title = if (uiState.place != null) uiState.place.name else "",
          subtitle = uiState.place?.address ?: "",
          image = uiState.place?.posts?.get(0)?.image ?: "",
          showBackButton = true,
          backOnClick = backOnClick,
          isLoading = uiState.place == null,
          buttonsRowContent = {

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
                value = uiState.place?.posts?.size.toString(),
                label = "Posts",
              )
            }
          }
        ) {
          if (uiState.place != null) {
            for (post in uiState.place.posts) {
              Post(
                postId = post.id,
                showDivider = true,
                showUser = true,
                placeOnClick = {  },
                userOnClick = { userOnClick(post.user?.id.toString()) },
                editOnClick = { editOnClick(post.id) },
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
private fun DetailsPlacePreview(
) {
  LocalLensTheme {
    DetailsPlace(uiState = DetailsPlaceUiState(place = SampleData.samplePlace))
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun DetailsPlaceLoadingPreview(
) {
  LocalLensTheme {
    DetailsPlace(uiState = DetailsPlaceUiState(place = null))
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun DetailsPlaceLongPreview(
) {
  val postManyPlaces = SampleData.samplePlace.copy(
    posts = listOf(
      SampleData.samplePost,
      SampleData.samplePost,
      SampleData.samplePost,
      SampleData.samplePost,
      SampleData.samplePost,
    ),
  )

  LocalLensTheme {
    DetailsPlace(uiState = DetailsPlaceUiState(place = postManyPlaces, loadingStates = LoadingStates.SUCCESS))

  }
}
