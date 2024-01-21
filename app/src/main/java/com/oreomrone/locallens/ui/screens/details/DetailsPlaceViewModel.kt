package com.oreomrone.locallens.ui.screens.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.ui.utils.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsPlaceViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  private val _uiState = MutableStateFlow(DetailsPlaceUiState())
  val uiState: StateFlow<DetailsPlaceUiState> = _uiState.asStateFlow()

  val id = savedStateHandle.get<String>("id")

  init {
    viewModelScope.launch { initializeUiState() }
  }

  private suspend fun initializeUiState() {
    if (id.isNullOrEmpty()) {
      logAndSetError("id is null or empty")
      return
    }

    viewModelScope.launch {
      val place = getPlace(id)

      if (place === null) {
        logAndSetError("place is null")
        return@launch
      }

      _uiState.update { currentState ->
        currentState.copy(
          place = place,
          loadingStates = LoadingStates.SUCCESS
        )
      }
    }
  }

  private suspend fun getPlace(id: String) : Place? {
    return SampleData.samplePlace // TODO
  }

  private fun logAndSetError(message: String) {
    Log.e(
      "DetailsPlaceViewModel",
      message
    )
    _uiState.update { currentState ->
      currentState.copy(
        loadingStates = LoadingStates.ERROR
      )
    }
  }
}

data class DetailsPlaceUiState(
  val place: Place? = null,
  val posts: List<Post> = emptyList(),
  val loadingStates: LoadingStates = LoadingStates.LOADING,
)
