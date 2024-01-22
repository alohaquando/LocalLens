package com.oreomrone.locallens.ui.screens.posts.newPost

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.placesAutocomplete.PlacesAutocompleteRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.PostVisibilities
import com.oreomrone.locallens.domain.dtoToDomain.asDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPostDetailsViewModel @Inject constructor(
  private val placesAutocompleteRepository: PlacesAutocompleteRepository,
) : ViewModel() {
  private val _uiState = MutableStateFlow(NewPostDetailsUiState())
  val uiState: StateFlow<NewPostDetailsUiState> = _uiState.asStateFlow()

  suspend fun performPost() {
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

  fun onVisibilityChange(visibility: PostVisibilities) {
    _uiState.update { currentState ->
      currentState.copy(
        visibility = visibility
      )
    }
  }

  //region Image
  fun onImageFileChange(imageFile: ByteArray) {
    _uiState.update { currentState ->
      currentState.copy(imageFile = imageFile)
    }
  }

  fun onImageChange(imageURL: String) {
    _uiState.update { currentState ->
      currentState.copy(imageURL = imageURL)
    }
    validateInput()
  }

  fun onImageRemove() {
    _uiState.update { currentState ->
      currentState.copy(imageURL = null)
    }
    validateInput()
  }
  //endregion

  private fun validateInput() {
    _uiState.update { currentState ->
      currentState.copy(
        inputValid = currentState.captionValid && currentState.imageURL != null && currentState.selectedPlace != null
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
        val places = placesAutocompleteRepository.getPlaceAutocompleteResults(_uiState.value.placeSearchQuery)

        _uiState.update { currentState ->
          currentState.copy(placeSearchResult = places.map { it.asDomainModel() })
        }
      }
    }
  }
  //endregion
}

data class NewPostDetailsUiState(
  // Input
  val caption: String = "",
  val visibility: PostVisibilities = PostVisibilities.PUBLIC,

  // Image
  val imageURL: String? = null,
  val imageFile: ByteArray? = null,

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
