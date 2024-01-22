package com.oreomrone.locallens.ui.screens.posts.newPost

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
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
fun NewPostDetails(
  backOnClick: () -> Unit = {},
  navigateToMe: () -> Unit = {}
) {
  val viewModel: NewPostDetailsViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  NewPostDetails(
    uiState = uiState,
    navigationButtonOnClick = backOnClick,
    navigateToMe = navigateToMe,
    onCaptionChange = viewModel::onCaptionChange,
    onImageChange = viewModel::onImageChange,
    onImageRemove = viewModel::onImageRemove,
    onImageFileChange = viewModel::onImageFileChange,
    submitOnClick = viewModel::performPost,
    onPlaceSearchQueryChange = viewModel::onPlaceSearchQueryChange,
    placeResultOnClick = viewModel::placeResultOnClick,
    onPlaceSearchEnter = viewModel::performUpdateAutocompleteResult,
    onVisibilityChange = viewModel::onVisibilityChange
  )
}

@Composable
private fun NewPostDetails(
  uiState: NewPostDetailsUiState = NewPostDetailsUiState(),
  navigationButtonOnClick: () -> Unit = {},
  navigateToMe: () -> Unit = {},
  onCaptionChange: (String) -> Unit = {},
  onImageChange: (String) -> Unit = {},
  onImageRemove: () -> Unit = {},
  onImageFileChange: (ByteArray) -> Unit = {},
  submitOnClick: suspend () -> Unit = {},
  onPlaceSearchQueryChange: (String) -> Unit = {},
  placeResultOnClick: (Place) -> Unit = {},
  onPlaceSearchEnter: () -> Unit = {},
  onVisibilityChange: (String) -> Unit = {},
) {
  PostInputLayout(
    title = "New post",
    navigationIcon = Icons.Default.ArrowBack,
    submitButtonText = "Post",
    caption = uiState.caption,
    imageURL = uiState.imageURL,
    inputValid = uiState.inputValid,
    captionValid = uiState.captionValid,
    alertDialogTitle = "Cancel post?",
    alertDialogText = "If you cancel now, your post will be lost.",
    alertDialogConfirmText = "Cancel post",
    snackbarHostState = uiState.snackBarHostState,
    navigationButtonOnClick = navigationButtonOnClick,
    onCaptionChange = onCaptionChange,
    onImageChange = onImageChange,
    onImageRemove = onImageRemove,
    onImageFileChange = onImageFileChange,
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
    navigateToMe()
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NewPostDetailsPreview(

) {
  LocalLensTheme {
    NewPostDetails(
      uiState = NewPostDetailsUiState()
    )
  }
}