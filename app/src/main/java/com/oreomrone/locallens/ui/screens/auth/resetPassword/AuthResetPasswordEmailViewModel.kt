package com.oreomrone.locallens.ui.screens.auth.resetPassword

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.oreomrone.locallens.ui.utils.validateEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AuthResetPasswordEmailViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(AuthResetPasswordEmailUiState())
  val uiState: StateFlow<AuthResetPasswordEmailUiState> = _uiState.asStateFlow()

  fun onEmailChange(newEmail: String) {
    _uiState.update { currentState ->
      currentState.copy(
        email = newEmail,
        isEmailValid = validateEmail(newEmail),
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

data class AuthResetPasswordEmailUiState(
  val email: String = "",
  val isEmailValid: Boolean = false,
  val snackbarHostState: SnackbarHostState = SnackbarHostState(),
  val isLoading: Boolean = false,
  val isVerifyEmailSent: Boolean = false,
)
