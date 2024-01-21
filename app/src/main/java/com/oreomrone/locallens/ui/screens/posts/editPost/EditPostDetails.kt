package com.oreomrone.locallens.ui.screens.posts.editPost

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.components.layouts.ErrorOverlay
import com.oreomrone.locallens.ui.components.layouts.PostInputLayout
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun EditPostDetails(
  backOnClick: () -> Unit = {},
) {
  val viewModel: EditPostDetailsViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  EditPostDetails(
    uiState = uiState,
    navigationButtonOnClick = backOnClick,
    onCaptionChange = viewModel::onCaptionChange,
    submitOnClick = viewModel::performSave,
    onPlaceSearchQueryChange = viewModel::onPlaceSearchQueryChange,
    placeResultOnClick = viewModel::placeResultOnClick,
    onPlaceSearchEnter = viewModel::performUpdateAutocompleteResult,
  )
}

@Composable
private fun EditPostDetails(
  uiState: EditPostDetailsUiState = EditPostDetailsUiState(),
  navigationButtonOnClick: () -> Unit = {},
  onCaptionChange: (String) -> Unit = {},
  submitOnClick: suspend () -> Unit = {},
  onPlaceSearchQueryChange: (String) -> Unit = {},
  placeResultOnClick: (Place) -> Unit = {},
  onPlaceSearchEnter: () -> Unit = {},
) {
  Crossfade(targetState =uiState.loadingState,
    label = "EditPostDetails loadingState crossfade"
  ) {
    when (it) {
      LoadingStates.LOADING -> {
        LoadingOverlay()
      }

      LoadingStates.ERROR -> {
        ErrorOverlay(
          backOnClick = navigationButtonOnClick,
        )
      }

      else -> {
        PostInputLayout(
          title = "Edit post",
          navigationIcon = Icons.Default.ArrowBack,
          submitButtonText = "Post",
          caption = uiState.caption,
          imageURL = uiState.imageURL,
          inputValid = uiState.inputValid,
          captionValid = uiState.captionValid,
          alertDialogTitle = "Cancel edit post?",
          alertDialogText = "If you cancel now, your edits won't be saved.",
          alertDialogConfirmText = "Cancel edit",
          snackbarHostState = uiState.snackBarHostState,
          navigationButtonOnClick = navigationButtonOnClick,
          onCaptionChange = onCaptionChange,
          onImageChange = {},
          onImageRemove = {},
          onImageFileChange = {},
          imageChangeEnabled = false,
          submitOnClick = submitOnClick,
          showPlaceError = uiState.showPlaceError,
          placeSearchQuery = uiState.placeSearchQuery,
          onPlaceSearchQueryChange = onPlaceSearchQueryChange,
          placeSearchResult = uiState.placeSearchResult,
          placeResultOnClick = placeResultOnClick,
          selectedPlace = uiState.selectedPlace,
          onPlaceSearchEnter = onPlaceSearchEnter,
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
private fun EditPostDetailsPreview(

) {
  LocalLensTheme {
    EditPostDetails(
      uiState = EditPostDetailsUiState()
    )
  }
}