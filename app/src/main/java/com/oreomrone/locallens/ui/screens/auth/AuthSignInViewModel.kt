package com.oreomrone.locallens.ui.screens.auth

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.auth.AuthRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.utils.validateEmail
import com.oreomrone.locallens.ui.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.gotrue.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthSignInViewModel @Inject constructor(
  val composeAuth: ComposeAuth,
  val auth: Auth,
  val authRepository: AuthRepository
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
      _uiState.update { currentState ->
        currentState.copy(
          loadingState = LoadingStates.LOADING
        )
      }

      val res = authRepository.signIn(
        _uiState.value.authSignInInfo.email,
        _uiState.value.authSignInInfo.password
      )

      if (res.first) {
        _uiState.update { currentState ->
          currentState.copy(
            loadingState = LoadingStates.SUCCESS
          )
        }
        _uiState.value.snackbarHostState.showSnackbar(res.second)
      } else {
        _uiState.update { currentState ->
          currentState.copy(
            loadingState = LoadingStates.ERROR
          )
        }
        _uiState.value.snackbarHostState.showSnackbar(res.second)
      }
    }
  }

  suspend fun performGoogleSignIn() {
    viewModelScope.launch {
      val result = authRepository.signInWithGoogle()
    }
  }

  suspend fun showSnackBar(message: String) {
    _uiState.value.snackbarHostState.showSnackbar(
      message = message,
    )
  }

  fun setLoading() {
    _uiState.update { currentState ->
      currentState.copy(
        loadingState = LoadingStates.LOADING
      )
    }
  }
}

data class AuthSignInUiState(
  val authSignInInfo: AuthSignInInfo = AuthSignInInfo(),
  val emailValid: Boolean = false,
  val passwordValid: Boolean = false,
  val inputValid: Boolean = false,
  val loadingState: LoadingStates = LoadingStates.IDLE,
  val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

data class AuthSignInInfo(
  val email: String = "",
  val password: String = ""
)