package com.oreomrone.locallens.ui.screens.accountSettings.manageOtherAccounts

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.User
import com.oreomrone.locallens.domain.dtoToDomain.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageOtherAccountsViewModel @Inject constructor(
  private val profileRepository: ProfileRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(ManageOtherAccountsUiState())
  val uiState: StateFlow<ManageOtherAccountsUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      initializeUiState()
    }
  }

  private suspend fun initializeUiState() {
    viewModelScope.launch {
      val accounts = getAllAccounts()

      _uiState.update { currentState ->
        currentState.copy(
          loadingState = LoadingStates.SUCCESS,
          accounts = accounts
        )
      }
    }
  }

  private suspend fun getAllAccounts(): List<User> {
    return profileRepository.getAllProfile().map { it.toUser() }
  }
}

data class ManageOtherAccountsUiState(
  val loadingState: LoadingStates = LoadingStates.LOADING,
  val accounts: List<User> = emptyList(),
)
