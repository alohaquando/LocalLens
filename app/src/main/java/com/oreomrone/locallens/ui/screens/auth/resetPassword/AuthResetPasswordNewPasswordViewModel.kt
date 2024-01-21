package com.oreomrone.locallens.ui.screens.auth.resetPassword


import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.oreomrone.locallens.ui.utils.validatePassword
import com.oreomrone.locallens.ui.utils.validatePasswordConfirm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AuthResetPasswordNewPasswordViewModel @Inject constructor(
) : ViewModel() {
  private val _uiState = MutableStateFlow(AuthResetPasswordNewPasswordUiState())
  val uiState: StateFlow<AuthResetPasswordNewPasswordUiState> = _uiState.asStateFlow()

  fun onNewPasswordChange(newPassword: String) {
    _uiState.update { currentState ->
      currentState.copy(
        newPassword = newPassword,
        newPasswordValid = validatePassword(newPassword)
      )
    }
    validateInput()
  }

  fun onNewPasswordConfirmChange(newPasswordConfirm: String) {
    _uiState.update { currentState ->
      currentState.copy(
        newPasswordConfirm = newPasswordConfirm,
        newPasswordConfirmValid = validatePasswordConfirm(
          newPasswordConfirm,
          currentState.newPassword
        )
      )
    }
    validateInput()
  }

  private fun validateInput() {
    _uiState.update { currentState ->
      currentState.copy(
        inputValid = currentState.newPasswordValid && currentState.newPasswordConfirmValid
      )
    }
  }

  suspend fun performChangePassword() {
    // TODO
  }
}

data class AuthResetPasswordNewPasswordUiState(
  val newPassword: String = "",
  val newPasswordConfirm: String = "",
  val newPasswordValid: Boolean = false,
  val newPasswordConfirmValid: Boolean = false,
  val inputValid: Boolean = false,
  val snackbarHostState: SnackbarHostState = SnackbarHostState(),
  val isLoading: Boolean = false,
  val isSuccessful: Boolean = false,
)