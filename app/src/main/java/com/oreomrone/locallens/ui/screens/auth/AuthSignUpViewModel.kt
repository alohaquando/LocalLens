package com.oreomrone.locallens.ui.screens.auth

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.auth.AuthRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.utils.validateEmail
import com.oreomrone.locallens.ui.utils.validatePassword
import com.oreomrone.locallens.ui.utils.validatePasswordConfirm
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.compose.auth.ComposeAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthSignUpViewModel @Inject constructor(
  private val authRepository: AuthRepository,
  val composeAuth: ComposeAuth
) : ViewModel() {
  private val _uiState = MutableStateFlow(AuthSignUpUiState())
  val uiState: StateFlow<AuthSignUpUiState> = _uiState.asStateFlow()

  fun updateUiState(authSignUpInfo: AuthSignUpInfo) {
    _uiState.update { currentState ->
      currentState.copy(
        authSignUpInfo = authSignUpInfo,
        emailValid = validateEmail(authSignUpInfo.email),
        passwordValid = validatePassword(authSignUpInfo.password),
        passwordConfirmValid = validatePasswordConfirm(
          authSignUpInfo.passwordConfirm,
          authSignUpInfo.password
        ),
      )
    }
  }

  suspend fun onSignUp() {
    viewModelScope.launch {
      _uiState.update { currentState ->
        currentState.copy(
          loadingState = LoadingStates.LOADING
        )
      }

      val res = authRepository.signUp(
        _uiState.value.authSignUpInfo.email,
        _uiState.value.authSignUpInfo.password
      )

      when (res.first) {
        true -> {
          _uiState.update { currentState ->
            currentState.copy(
              loadingState = LoadingStates.SUCCESS
            )
          }
          _uiState.value.snackbarHostState.showSnackbar(res.second)
        }

        else -> {
          _uiState.update { currentState ->
            currentState.copy(
              loadingState = LoadingStates.ERROR
            )
          }
          _uiState.value.snackbarHostState.showSnackbar(res.second)
        }
      }
    }
  }

  suspend fun performGoogleSignIn() {
    viewModelScope.launch {
      authRepository.signInWithGoogle()
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

data class AuthSignUpUiState(
  val authSignUpInfo: AuthSignUpInfo = AuthSignUpInfo(),
  val loadingState: LoadingStates = LoadingStates.IDLE,
  val emailValid: Boolean = false,
  val passwordValid: Boolean = false,
  val passwordConfirmValid: Boolean = false,
  val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

data class AuthSignUpInfo(
  val email: String = "",
  val password: String = "",
  val passwordConfirm: String = ""
)
