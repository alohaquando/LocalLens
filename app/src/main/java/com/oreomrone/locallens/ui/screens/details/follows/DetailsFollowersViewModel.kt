package com.oreomrone.locallens.ui.screens.details.follows

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsFollowersViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  private val _uiState = MutableStateFlow(DetailsFollowersUiState())
  val uiState: StateFlow<DetailsFollowersUiState> = _uiState.asStateFlow()

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
      val followers = getFollowers(userId = id)

      _uiState.update { currentState ->
        currentState.copy(
          loadingState = LoadingStates.SUCCESS,
          followers = followers
        )
      }
    }
  }

  private suspend fun getFollowers(userId: String): List<User> {
    return emptyList()    // TODO
  }

  private fun logAndSetError(message: String) {
    Log.e(
      "DetailsFollowersViewModel",
      message
    )
    _uiState.update { currentState ->
      currentState.copy(
        loadingState = LoadingStates.ERROR
      )
    }
  }
}

data class DetailsFollowersUiState(
  val loadingState: LoadingStates = LoadingStates.LOADING,
  val followers: List<User> = emptyList(),
)
