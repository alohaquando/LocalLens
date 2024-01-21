package com.oreomrone.locallens.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.navigation.AppNavDests
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartDestinationViewModel @Inject constructor(
  private val auth: Auth,
  private val profileRepository: ProfileRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(AuthUiState())
  val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      auth.sessionStatus.collect {
        when (it) {
          // when user is authenticated
          is SessionStatus.Authenticated
          -> {
            // Get their profile
            val sessionProfile = it.session.user?.let { sessionUser ->
              profileRepository.getProfileById(sessionUser.id)
            }

            if (sessionProfile !== null) {
              // if profile exists
              when (sessionProfile.username.isBlank()) {
                // and if username is blank
                true  -> {
                  _uiState.update { currentState ->
                    currentState.copy(
                      startingDestination = AppNavDests.CompleteAccountProfile.name,
                      loadingStates = LoadingStates.SUCCESS
                    )
                  }
                }

                // or if username is not blank
                false -> {
                  _uiState.update { currentState ->
                    currentState.copy(
                      startingDestination = AppNavDests.Posts.name,
                      loadingStates = LoadingStates.SUCCESS
                    )
                  }
                }
              }
            } else{
              _uiState.update { currentState ->
                currentState.copy(
                  startingDestination = AppNavDests.AuthSignIn.name,
                  loadingStates = LoadingStates.SUCCESS
                )
              }
            }
          }


          // when user isn't authenticated
          SessionStatus.NetworkError, SessionStatus.LoadingFromStorage, SessionStatus.NotAuthenticated -> {
            _uiState.update { currentState ->
              currentState.copy(
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
  val startingDestination: String? = null,
  val loadingStates: LoadingStates = LoadingStates.LOADING
)
