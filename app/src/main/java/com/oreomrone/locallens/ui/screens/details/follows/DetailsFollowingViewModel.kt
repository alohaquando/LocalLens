package com.oreomrone.locallens.ui.screens.details.follows

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
class DetailsFollowingViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val profileRepository: ProfileRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(DetailsFollowingUiState())
  val uiState: StateFlow<DetailsFollowingUiState> = _uiState.asStateFlow()

  val id = savedStateHandle.get<String>("id")

  init {
    viewModelScope.launch {
      initializeUiState()
    }
  }

  private suspend fun initializeUiState() {
    if (id === null) {
      logAndSetError("id is null")
      return
    }

    viewModelScope.launch {
      val following = getFollowing(id)

      _uiState.update { currentState ->
        currentState.copy(
          loadingState = LoadingStates.SUCCESS,
          following = following
        )
      }
    }
  }

  private suspend fun getFollowing(userId: String): List<User> {
    return profileRepository.getFollowingsById(userId).map { it.toUser() }
  }

  private fun logAndSetError(message: String) {
    Log.e(
      "DetailsFollowingViewModel",
      message
    )
    _uiState.update { currentState ->
      currentState.copy(
        loadingState = LoadingStates.ERROR
      )
    }
  }
}

data class DetailsFollowingUiState(
  val loadingState: LoadingStates = LoadingStates.LOADING,
  val following: List<User> = emptyList(),
)
