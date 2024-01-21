package com.oreomrone.locallens.ui.screens.posts.newPost

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.ui.components.layouts.PostInputLayout
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun NewPostDetails(
  backOnClick: () -> Unit = {},
) {
  val viewModel: NewPostDetailsViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  NewPostDetails(
    uiState = uiState,
    navigationButtonOnClick = backOnClick,
    onCaptionChange = viewModel::onCaptionChange,
    onImageChange = viewModel::onImageChange,
    onImageRemove = viewModel::onImageRemove,
    onImageFileChange = viewModel::onImageFileChange,
    submitOnClick = viewModel::performPost,
    onPlaceSearchQueryChange = viewModel::onPlaceSearchQueryChange,
    placeResultOnClick = viewModel::placeResultOnClick,
    onPlaceSearchEnter = viewModel::performUpdateAutocompleteResult,
  )
}

@Composable
private fun NewPostDetails(
  uiState: NewPostDetailsUiState = NewPostDetailsUiState(),
  navigationButtonOnClick: () -> Unit = {},
  onCaptionChange: (String) -> Unit = {},
  onImageChange: (String) -> Unit = {},
  onImageRemove: () -> Unit = {},
  onImageFileChange: (ByteArray) -> Unit = {},
  submitOnClick: suspend () -> Unit = {},
  onPlaceSearchQueryChange: (String) -> Unit = {},
  placeResultOnClick: (Place) -> Unit = {},
  onPlaceSearchEnter: () -> Unit = {},
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
  )
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