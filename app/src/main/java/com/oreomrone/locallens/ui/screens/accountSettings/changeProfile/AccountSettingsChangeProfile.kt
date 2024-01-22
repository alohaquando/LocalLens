package com.oreomrone.locallens.ui.screens.accountSettings.changeProfile

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
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.components.layouts.ErrorOverlay
import com.oreomrone.locallens.ui.components.layouts.ProfileInputLayout
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun AccountSettingsChangeProfile(
  backOnClick: () -> Unit = {},
) {
  val viewModel: AccountSettingsChangeProfileViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  AccountSettingsChangeProfile(
    uiState = uiState,
    backOnClick = backOnClick,
    onNameChange = viewModel::onNameChange,
    onUsernameChange = viewModel::onUsernameChange,
    onIsPrivateChange = viewModel::onIsPrivateChange,
    onBioChange = viewModel::onBioChange,
    onImageChange = viewModel::onImageChange,
    onImageRemove = viewModel::onImageRemove,
    onImageFileChange = viewModel::onImageFileChange,
    performSave = viewModel::performSave
  )
}

@Composable
private fun AccountSettingsChangeProfile(
  uiState: AccountSettingsChangeProfileUiState = AccountSettingsChangeProfileUiState(),
  backOnClick: () -> Unit = {},
  onNameChange: (String) -> Unit = {},
  onUsernameChange: (String) -> Unit = {},
  onBioChange: (String) -> Unit = {},
  onIsPrivateChange: (Boolean) -> Unit = {},
  onImageChange: (String) -> Unit = {},
  onImageRemove: () -> Unit = {},
  onImageFileChange: (ByteArray) -> Unit = {},
  performSave: suspend () -> Unit = {},
) {
  ProfileInputLayout(
    title = "Edit your profile",
    navigationIcon = Icons.Default.ArrowBack,
    submitButtonText = "Save",
    username = uiState.username,
    name = uiState.name,
    bio = uiState.bio,
    isPrivate = uiState.isPrivate,
    imageURL = uiState.imageURL,
    inputValid = uiState.inputValid,
    usernameValid = uiState.usernameValid,
    nameValid = uiState.nameValid,
    bioValid = uiState.bioValid,
    snackbarHostState = uiState.snackbarHostState,
    navigationButtonOnClick = backOnClick,
    onNameChange = onNameChange,
    onUsernameChange = onUsernameChange,
    onBioChange = onBioChange,
    onIsPrivateChange = onIsPrivateChange,
    onImageChange = onImageChange,
    onImageRemove = onImageRemove,
    onImageFileChange = onImageFileChange,
    submitOnClick = performSave,
  )

  AnimatedVisibility(
    visible = uiState.loadingState === LoadingStates.ERROR,
    enter = fadeIn(),
    exit = fadeOut()
  ) {
    ErrorOverlay(backOnClick = backOnClick)
  }

  AnimatedVisibility(
    visible = uiState.loadingState === LoadingStates.LOADING,
    enter = fadeIn(),
    exit = fadeOut()
  ) {
    LoadingOverlay()
  }

  if (uiState.loadingState === LoadingStates.SUCCESS) {
    backOnClick()
  }
}


@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AccountSettingChangeProfileHasInfoPreview(

) {
  LocalLensTheme {
    AccountSettingsChangeProfile(
      uiState = AccountSettingsChangeProfileUiState(
        name = "Sheen Hahn",
        username = "the_sheenathan",
        bio = "I love to travel and take photos!",
        imageURL = "https://images.unsplash.com/photo-1593757147298-e064ed1419e5?q=80&w=3687&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
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
private fun AccountSettingChangeProfilePreview(

) {
  LocalLensTheme {
    AccountSettingsChangeProfile()
  }
}

