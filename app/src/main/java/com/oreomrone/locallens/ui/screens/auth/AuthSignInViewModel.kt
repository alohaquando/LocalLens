package com.oreomrone.locallens.ui.screens.auth

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.ui.utils.validateEmail
import com.oreomrone.locallens.ui.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthSignInViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(AuthSignInUiState())
  val uiState: StateFlow<AuthSignInUiState> = _uiState.asStateFlow()

   fun updateUiState(authSignInInfo: AuthSignInInfo) {
    _uiState.update { currentState ->
      currentState.copy(
        authSignInInfo = authSignInInfo,
        inputValid = validateInput(),
        emailValid = validateEmail(authSignInInfo.email),
        passwordValid = validatePassword(authSignInInfo.password),
      )
    }
  }

  private fun validateInput(): Boolean {
    return validateEmail(_uiState.value.authSignInInfo.email) && validatePassword(
      _uiState.value.authSignInInfo.password
    )
  }

  suspend fun performSignIn() {
    viewModelScope.launch {
      // TODO

      _uiState.value.snackbarHostState.showSnackbar(
        message = "Something",
      )
    }
  }

  suspend fun performGoogleSignIn() {
    viewModelScope.launch {
      // TODO
    }
  }
}

data class AuthSignInUiState(
  val authSignInInfo: AuthSignInInfo = AuthSignInInfo(),
  val emailValid: Boolean = false,
  val passwordValid: Boolean = false,
  val inputValid: Boolean = false,
  val isLoading: Boolean = false,
  val isSuccess: Boolean = false,
  val isFailure: Boolean = false,
  val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

data class AuthSignInInfo(
  val email: String = "",
  val password: String = ""
)