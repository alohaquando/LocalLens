package com.oreomrone.locallens.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.navigation.AppNavDests
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val auth: Auth
) : ViewModel() {
  private val _uiState = MutableStateFlow(AuthUiState())
  val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      auth.sessionStatus.collect {
        when (it) {
          // when user is authenticated
          is SessionStatus.Authenticated                                                               -> {
            _uiState.update { currentState ->
              currentState.copy(
                session = it.session,
                startingDestination = AppNavDests.Posts.name,
                loadingStates = LoadingStates.SUCCESS
              )
            }
          }

          // when user is authenticated but haven't completed their account
          // TODO

          // when user isn't authenticated
          SessionStatus.NetworkError, SessionStatus.LoadingFromStorage, SessionStatus.NotAuthenticated -> {
            _uiState.update { currentState ->
              currentState.copy(
                session = null,
                startingDestination = AppNavDests.AuthSignIn.name,
                loadingStates = LoadingStates.SUCCESS
              )
            }
          }
        }
      }
    }
  }
}

data class AuthUiState(
  val session: UserSession? = null,
  val startingDestination: String? = null,
  val loadingStates: LoadingStates = LoadingStates.LOADING
)
