package com.oreomrone.locallens.ui.screens.posts.editPost

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.placesAutocomplete.PlacesAutocompleteRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.domain.dtoToDomain.asDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPostDetailsViewModel @Inject constructor(
  private val placesAutocompleteRepository: PlacesAutocompleteRepository,
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  private val _uiState = MutableStateFlow(EditPostDetailsUiState())
  val uiState: StateFlow<EditPostDetailsUiState> = _uiState.asStateFlow()

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
      val post = getPost(id)

      if (post === null) {
        logAndSetError("post is null")
        return@launch
      }

      _uiState.update { currentState ->
        currentState.copy(
          caption = post.caption,
          imageURL = post.image.toString(),
          captionValid = true,
          inputValid = true,
          selectedPlace = post.place,
          placeSearchQuery = post.place.name,
          loadingState = LoadingStates.SUCCESS
        )
      }
    }
  }

  private suspend fun getPost(id: String): Post? {
    return null// TODO
  }

  private fun logAndSetError(message: String) {
    Log.e(
      "EditPostDetailsViewModel",
      message
    )
    _uiState.update { currentState ->
      currentState.copy(
        loadingState = LoadingStates.ERROR
      )
    }
  }

  suspend fun performSave() {
    // TODO
  }

  fun onCaptionChange(caption: String) {
    _uiState.update { currentState ->
      currentState.copy(
        caption = caption,
        captionValid = caption.isNotBlank()
      )
    }
    validateInput()
  }

  private fun validateInput() {
    _uiState.update { currentState ->
      currentState.copy(
        inputValid = currentState.captionValid && currentState.selectedPlace != null
      )
    }
  }

  //region Places Autocomplete
  fun onPlaceSearchQueryChange(query: String) {
    _uiState.update { currentState ->
      currentState.copy(
        placeSearchQuery = query
      )
    }
    if (query.isEmpty()) {
      clearAutocompleteResults()
    }
  }

  fun placeResultOnClick(place: Place) {
    _uiState.update { currentState ->
      currentState.copy(
        selectedPlace = place,
      )
    }
  }

  private fun clearAutocompleteResults() {
    _uiState.update { currentState ->
      currentState.copy(placeSearchResult = emptyList())
    }
  }

  fun performUpdateAutocompleteResult() {
    clearAutocompleteResults()
    viewModelScope.launch {
      updateAutocompleteResult()
    }
  }

  private fun updateAutocompleteResult() {
    viewModelScope.launch {
      if (_uiState.value.placeSearchQuery.isNotEmpty()) {
        val places =
          placesAutocompleteRepository.getPlaceAutocompleteResults(_uiState.value.placeSearchQuery)

        _uiState.update { currentState ->
          currentState.copy(placeSearchResult = places.map { it.asDomainModel() })
        }
      }
    }
  }
  //endregion
}

data class EditPostDetailsUiState(
  // Input
  val caption: String = "",

  // Image
  val imageURL: String? = null,

  // Validation
  val showPlaceError: Boolean = false,
  val captionValid: Boolean = false,
  val inputValid: Boolean = false,

  // Place Search
  val placeSearchQuery: String = "",
  val placeSearchResult: List<Place> = emptyList(),
  val selectedPlace: Place? = null,

  // States
  val loadingState: LoadingStates = LoadingStates.LOADING,

  // Snackbar
  val snackBarHostState: SnackbarHostState = SnackbarHostState(),
)
