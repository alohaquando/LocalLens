package com.oreomrone.locallens.ui.screens.auth

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.ui.utils.validateEmail
import com.oreomrone.locallens.ui.utils.validatePassword
import com.oreomrone.locallens.ui.utils.validatePasswordConfirm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthSignUpViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(AuthSignUpUiState())
  val uiState: StateFlow<AuthSignUpUiState> = _uiState.asStateFlow()

  fun updateUiState(authSignUpInfo: AuthSignUpInfo) {
    _uiState.update { currentState ->
      currentState.copy(
        authSignUpInfo = authSignUpInfo,
        emailValid = validateEmail(authSignUpInfo.email),
        passwordValid = validatePassword(authSignUpInfo.password),
        passwordConfirmValid = validatePasswordConfirm(authSignUpInfo.passwordConfirm,
          authSignUpInfo.password),
      )
    }
  }

  suspend fun onSignUp() {
    viewModelScope.launch {
      // TODO
    }
  }

  suspend fun onSignUpWithGoogle() {
    viewModelScope.launch {
      // TODO
    }
  }
}

data class AuthSignUpUiState(
  val authSignUpInfo: AuthSignUpInfo = AuthSignUpInfo(),
  val isLoading: Boolean = false,
  val emailValid: Boolean = false,
  val passwordValid: Boolean = false,
  val passwordConfirmValid: Boolean = false,
  val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

data class AuthSignUpInfo(
  val email: String = "", val password: String = "", val passwordConfirm: String = ""
)
