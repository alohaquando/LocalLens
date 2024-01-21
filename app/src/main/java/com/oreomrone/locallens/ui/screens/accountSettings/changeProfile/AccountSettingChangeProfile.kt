package com.oreomrone.locallens.ui.screens.accountSettings.changeProfile

import android.content.res.Configuration
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
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.components.layouts.ErrorOverlay
import com.oreomrone.locallens.ui.components.layouts.ProfileInputLayout
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun AccountSettingChangeProfile(
  backOnClick: () -> Unit = {},
) {
  val viewModel: AccountSettingChangeProfileViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  AccountSettingChangeProfile(
    uiState = uiState,
    backOnClick = backOnClick,
    onNameChange = viewModel::onNameChange,
    onUsernameChange = viewModel::onUsernameChange,
    onBioChange = viewModel::onBioChange,
    onImageChange = viewModel::onImageChange,
    onImageRemove = viewModel::onImageRemove,
    onImageFileChange = viewModel::onImageFileChange,
    performSave = viewModel::performSave
  )
}

@Composable
private fun AccountSettingChangeProfile(
  uiState: AccountSettingChangeProfileUiState = AccountSettingChangeProfileUiState(),
  backOnClick: () -> Unit = {},
  onNameChange: (String) -> Unit = {},
  onUsernameChange: (String) -> Unit = {},
  onBioChange: (String) -> Unit = {},
  onImageChange: (String) -> Unit = {},
  onImageRemove: () -> Unit = {},
  onImageFileChange: (ByteArray) -> Unit = {},
  performSave: suspend () -> Unit = {},
) {
  Crossfade(
    targetState = uiState.loadingState,
    label = "AccountSettingChangeProfile Crossfade"
  ) {
    when (it) {
      LoadingStates.LOADING -> {
        LoadingOverlay()
      }

      LoadingStates.ERROR   -> {
        ErrorOverlay(
          backOnClick = backOnClick
        )
      }

      LoadingStates.SUCCESS -> {
        ProfileInputLayout(
          title = "Edit your profile",
          navigationIcon = Icons.Default.ArrowBack,
          submitButtonText = "Save",
          username = uiState.username,
          name = uiState.name,
          bio = uiState.bio,
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
          onImageChange = onImageChange,
          onImageRemove = onImageRemove,
          onImageFileChange = onImageFileChange,
          submitOnClick = performSave,
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
private fun AccountSettingChangeProfileHasInfoPreview(

) {
  LocalLensTheme {
    AccountSettingChangeProfile(
      uiState = AccountSettingChangeProfileUiState(
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
    AccountSettingChangeProfile()
  }
}

