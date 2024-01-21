package com.oreomrone.locallens.ui.screens.accountSettings.changePassword

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AccountSettingsChangePasswordConfirmViewModel @Inject constructor(
) : ViewModel() {
  private val _uiState = MutableStateFlow(AccountSettingsChangePasswordConfirmUiState())
  val uiState: StateFlow<AccountSettingsChangePasswordConfirmUiState> = _uiState.asStateFlow()

  fun onCurrentPasswordChange(password: String) {
    _uiState.update { currentState ->
      currentState.copy(
        currentPassword = password,
      )
    }
  }

  suspend fun performValidateCurrentPassword() {
    // TODO
    if (true) {
      _uiState.update { currentState ->
        currentState.copy(
          isLoading = false,
          isCurrentPasswordValid = true,
        )
      }
    }
  }
}

data class AccountSettingsChangePasswordConfirmUiState(
  val currentPassword: String = "",
  val snackbarHostState: SnackbarHostState = SnackbarHostState(),
  val isLoading: Boolean = false,
  val isCurrentPasswordValid: Boolean = false,
)
