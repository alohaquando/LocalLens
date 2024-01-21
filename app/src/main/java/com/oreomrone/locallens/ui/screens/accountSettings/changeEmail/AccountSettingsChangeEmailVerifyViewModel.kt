package com.oreomrone.locallens.ui.screens.accountSettings.changeEmail

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.oreomrone.locallens.ui.utils.validatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class AccountSettingsChangeEmailVerifyViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(AccountSettingsChangeEmailVerifyUiState())
  val uiState: StateFlow<AccountSettingsChangeEmailVerifyUiState> = _uiState.asStateFlow()

  fun onVerificationCodeChange(verificationCode: String) {
    _uiState.update { currentState ->
      currentState.copy(
        verificationCode = verificationCode,
      )
    }
  }

  suspend fun performValidateVerificationCode() {
    // TODO
    if (true) {
      _uiState.update { currentState ->
        currentState.copy(
          isLoading = false,
          isVerificationCodeValid = true,
        )
      }
    }
  }
}

data class AccountSettingsChangeEmailVerifyUiState(
  val verificationCode: String = "",
  val snackbarHostState: SnackbarHostState = SnackbarHostState(),
  val isLoading: Boolean = false,
  val isVerificationCodeValid: Boolean = false,
)
