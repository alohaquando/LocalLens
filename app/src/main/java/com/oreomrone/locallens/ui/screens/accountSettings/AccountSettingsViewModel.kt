package com.oreomrone.locallens.ui.screens.accountSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
  private val authRepository: AuthRepository,
) : ViewModel() {
  private val _uiState = MutableStateFlow(AccountSettingsUiState())
  val uiState: StateFlow<AccountSettingsUiState> = _uiState.asStateFlow()

  suspend fun performSignOut() {
    viewModelScope.launch {
      val res = authRepository.signOut()
      _uiState.update { currentState ->
        currentState.copy(isSignedOut = res)
      }
    }
  }
}

data class AccountSettingsUiState(
  val isSuperUser: Boolean = false,
  val isSignedOut: Boolean = false,
)
