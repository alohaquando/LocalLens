package com.oreomrone.locallens.ui.screens.posts.editPost

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
    onVisibilityChange = viewModel::onVisibilityChange
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
  onVisibilityChange: (String) -> Unit = {},
  ) {
  PostInputLayout(
    title = "Edit post",
    navigationIcon = Icons.Default.ArrowBack,
    submitButtonText = "Post",
    caption = uiState.caption,
    imageURL = uiState.imageURL,
    inputValid = uiState.inputValid,
    captionValid = uiState.captionValid,
    imageChangeEnabled = false,
    alertDialogTitle = "Cancel editing post?",
    alertDialogText = "If you cancel now, your edits will be lost.",
    alertDialogConfirmText = "Cancel edits",
    snackbarHostState = uiState.snackBarHostState,
    navigationButtonOnClick = navigationButtonOnClick,
    onCaptionChange = onCaptionChange,
    submitOnClick = submitOnClick,
    showPlaceError = uiState.showPlaceError,
    placeSearchQuery = uiState.placeSearchQuery,
    onPlaceSearchQueryChange = onPlaceSearchQueryChange,
    placeSearchResult = uiState.placeSearchResult,
    placeResultOnClick = placeResultOnClick,
    selectedPlace = uiState.selectedPlace,
    onPlaceSearchEnter = onPlaceSearchEnter,
    visibility = uiState.visibility,
    onVisibilityChange = onVisibilityChange,
  )

  AnimatedVisibility(
    visible = (uiState.loadingState == LoadingStates.LOADING),
    enter = fadeIn(),
    exit = fadeOut()
  ) {
    LoadingOverlay()
  }

  if (uiState.loadingState == LoadingStates.ERROR) {
    ErrorOverlay(backOnClick = navigationButtonOnClick)
  }

  if (uiState.loadingState == LoadingStates.SUCCESS) {
    navigationButtonOnClick()
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
      uiState = EditPostDetailsUiState(
        loadingState = LoadingStates.IDLE
      )
    )
  }
}