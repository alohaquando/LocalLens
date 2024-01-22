package com.oreomrone.locallens.ui.screens.completeAccount

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.components.layouts.ProfileInputLayout
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun CompleteAccountProfile(
  navigateToMe: () -> Unit = {},
) {
  val viewModel: CompleteAccountProfileViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  CompleteAccountProfile(
    uiState = uiState,
    closeOnClick = viewModel::performSignOut,
    onNameChange = viewModel::onNameChange,
    onUsernameChange = viewModel::onUsernameChange,
    onBioChange = viewModel::onBioChange,
    onIsPrivateChange = viewModel::onIsPrivateChange,
    onImageChange = viewModel::onImageChange,
    onImageRemove = viewModel::onImageRemove,
    onImageFileChange = viewModel::onImageFileChange,
    onPerformComplete = viewModel::performComplete,
    navigateToMe = navigateToMe
  )
}

@Composable
private fun CompleteAccountProfile(
  uiState: CompleteAccountProfileUiState = CompleteAccountProfileUiState(),
  onIsPrivateChange: (Boolean) -> Unit = {},
  closeOnClick: suspend () -> Unit = {},
  onNameChange: (String) -> Unit = {},
  onUsernameChange: (String) -> Unit = {},
  onBioChange: (String) -> Unit = {},
  onImageChange: (String) -> Unit = {},
  onImageRemove: () -> Unit = {},
  onImageFileChange: (ByteArray) -> Unit = {},
  onPerformComplete: suspend () -> Unit = {},
  navigateToMe: () -> Unit = {},
) {
  if (uiState.loadingState === LoadingStates.SUCCESS) {
    navigateToMe()
  }

  ProfileInputLayout(
    title = "Complete your profile",
    navigationIcon = Icons.Rounded.Close,
    submitButtonText = "Complete",
    username = uiState.username,
    name = uiState.name,
    bio = uiState.bio,
    isPrivate = uiState.isPrivate,
    imageURL = uiState.imageURL,
    inputValid = uiState.inputValid,
    usernameValid = uiState.usernameValid,
    nameValid = uiState.nameValid,
    bioValid = uiState.bioValid,
    alertDialogTitle = "Stop completing your profile and sign out?",
    alertDialogText = "Your profile must be completed before you can continue. Any information you added won't be saved if you sign out.",
    alertDialogConfirmText = "Sign out",
    snackbarHostState = uiState.snackbarHostState,
    navigationButtonOnClick = closeOnClick,
    onNameChange = onNameChange,
    onUsernameChange = onUsernameChange,
    onBioChange = onBioChange,
    onIsPrivateChange = onIsPrivateChange,
    onImageChange = onImageChange,
    onImageRemove = onImageRemove,
    onImageFileChange = onImageFileChange,
    submitOnClick = onPerformComplete,
  )

  AnimatedVisibility(
    visible = uiState.loadingState === LoadingStates.LOADING,
    enter = fadeIn(),
    exit = fadeOut()
  ) {
    LoadingOverlay()
  }
}


@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CompleteAccountProfileHasInfoPreview(

) {
  LocalLensTheme {
    CompleteAccountProfile(
      uiState = CompleteAccountProfileUiState(
        name = "Sheen Hahn",
        username = "the_sheenathan",
        bio = "I love to travel and take photos!",
        imageURL = "https://images.unsplash" + ".com/photo-1593757147298-e064ed1419e5?q=80&w=3687&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        loadingState = LoadingStates.IDLE
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
private fun CompleteAccountProfilePreview(

) {
  LocalLensTheme {
    CompleteAccountProfile(
      uiState = CompleteAccountProfileUiState(
        loadingState = LoadingStates.LOADING
      )
    )
  }
}

