package com.oreomrone.locallens.ui.screens.discover

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.components.Post
import com.oreomrone.locallens.ui.components.layouts.ErrorOverlay
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.PostViewModel
import com.oreomrone.locallens.ui.utils.SampleData
import kotlinx.coroutines.launch

@Composable
fun Discover(
  searchBarOnClick: () -> Unit = {},
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {}
) {
  val viewModel: DiscoverViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val postViewModel: PostViewModel = hiltViewModel()

  val context = LocalContext.current
  val packageManager = context.packageManager

  Discover(
    uiState = uiState,
    searchBarOnClick = searchBarOnClick,
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
    refreshOnClick = viewModel::initializeUiState,
  )
}

@Composable
private fun Discover(
  uiState: DiscoverUiState = DiscoverUiState(),
  searchBarOnClick: () -> Unit = {},
  navigateOnClick: (Double, Double, String) -> Unit = { _, _, _ -> },
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {},
  favoriteOnClick: suspend (String) -> Unit = {},
  deleteOnClick: suspend (String) -> Unit = {},
  refreshOnClick:suspend () -> Unit = {},
) {
  val coroutineScope = rememberCoroutineScope()

  Scaffold(
    topBar = {
      Surface(color = MaterialTheme.colorScheme.surface) {
        Column {
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(
                top = WindowInsets.systemBars
                  .asPaddingValues()
                  .calculateTopPadding()
              )
              .padding(
                horizontal = 16.dp,
                vertical = 8.dp
              )
              .height(56.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(99.dp))
                .background(
                  MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
                .clickable { searchBarOnClick() },
              contentAlignment = Alignment.CenterStart
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Search for a place or a person",
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                  imageVector = Icons.Rounded.Search,
                  contentDescription = "Search",
                )
              }
            }
          }

          Divider(modifier = Modifier.padding(top = 8.dp))
        }
      }
    },
    bottomBar = {
      // TODO
    },

    ) { innerPadding ->

    Crossfade(
      targetState = uiState.loadingStates,
      label = "Details layout animation for Discover"
    ) {
      when (it) {

        // Loading
        LoadingStates.LOADING -> {
          LoadingOverlay()
        }

        LoadingStates.ERROR   -> {
          ErrorOverlay(
            showBackButton = false
          )
        }

        // All posts
        else                  -> {
          Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
            if (uiState.posts.isEmpty()) {
              Text(
                text = "No posts yet",
                textAlign = TextAlign.Center,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(
                    horizontal = 16.dp,
                    vertical = 24.dp
                  )
                  .alpha(0.7f)
              )
            } else {
              for (post in uiState.posts) {
                Post(postId = post.id,
                  showDivider = true,
                  showUser = true,
                  placeOnClick = { placeOnClick(post.place.id) },
                  userOnClick = { userOnClick(post.user?.id.toString()) },
                  editOnClick = { editOnClick(post.id) },
                  afterDeletionCallback = {
                    coroutineScope.launch {
                      refreshOnClick()
                    }
                  })
              }
            }
          }
          Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
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
private fun DiscoverPreview(

) {
  LocalLensTheme {
    Discover(
      DiscoverUiState(
        posts = listOf(
          SampleData.samplePost,
          SampleData.samplePost1
        )
      )
    )
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DiscoverEmptyPreview(

) {
  LocalLensTheme {
    Discover(
      DiscoverUiState(
        loadingStates = LoadingStates.SUCCESS,
      )
    )
  }
}