package com.oreomrone.locallens.ui.screens.me

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.domain.User
import com.oreomrone.locallens.domain.dtoToDomain.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
  private val profileRepository: ProfileRepository,
  private val auth: Auth,
) : ViewModel() {
  private val _uiState = MutableStateFlow(MeUiState())
  val uiState: StateFlow<MeUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      auth.currentSessionOrNull()?.user?.let {
        val profile = profileRepository.getProfileById(it.id)
        val user = profile?.toUser()

        when (user === null) {
          true  -> {
            Log.e(
              "MeViewModel",
              "user is null"
            )
            _uiState.update { currentState ->
              currentState.copy(isError = true)
            }
          }

          false -> {
            _uiState.update { currentState ->
              currentState.copy(user = user)
            }
          }
        }
      }
    }
  }

  fun updateUiState(uiState: MeUiState) {
    viewModelScope.launch {
      _uiState.update { uiState }
    }
  }


}

data class MeUiState(
  val user: User? = null,
  val isError: Boolean = false,
)
