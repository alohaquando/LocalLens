package com.oreomrone.locallens.ui.screens.accountSettings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AccountSettingsViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(AccountSettingsUiState())
  val uiState: StateFlow<AccountSettingsUiState> = _uiState.asStateFlow()

  suspend fun performSignOut() {
    // TODO
  }
}

data class AccountSettingsUiState(
  val isSuperUser: Boolean = false,
)
