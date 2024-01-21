package com.oreomrone.locallens.ui.screens.accountSettings.changeEmail

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.ui.utils.validateEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountSettingsChangeEmailNewViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(AccountSettingsChangeEmailNewUiState())
  val uiState: StateFlow<AccountSettingsChangeEmailNewUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      getCurrentEmail()
    }
  }

  suspend fun getCurrentEmail() {
    // TODO
    if (true) {
      _uiState.update { currentState ->
        currentState.copy(
          currentEmail = ""
        )
      }
    }
  }

  fun onNewEmailChange(newEmail: String) {
    _uiState.update { currentState ->
      currentState.copy(
        newEmail = newEmail,
        isNewEmailValid = validateEmail(newEmail),
      )
    }
  }

  suspend fun performSendVerifyEmail() {
    // TODO
    if (true) {
      _uiState.update { currentState ->
        currentState.copy(
          isLoading = false,
          isVerifyEmailSent = true,
        )
      }
    }
  }
}

data class AccountSettingsChangeEmailNewUiState(
  val currentEmail: String = "example@email.com",
  val newEmail: String = "",
  val isNewEmailValid: Boolean = false,
  val snackbarHostState: SnackbarHostState = SnackbarHostState(),
  val isLoading: Boolean = false,
  val isVerifyEmailSent: Boolean = false,
)
