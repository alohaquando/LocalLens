package com.oreomrone.locallens.ui.screens.posts.newPost

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.placesAutocomplete.PlacesAutocompleteRepository
import com.oreomrone.locallens.data.repositories.post.PostRepository
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
  private val postRepository: PostRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(NewPostDetailsUiState())
  val uiState: StateFlow<NewPostDetailsUiState> = _uiState.asStateFlow()

  suspend fun performPost() {
    viewModelScope.launch {
      _uiState.update { currentState ->
        currentState.copy(
          loadingState = LoadingStates.LOADING
        )
      }

      val res = postRepository.createPost(
        imageFile = _uiState.value.imageFile,
        imageUrl = _uiState.value.imageURL,
        caption = _uiState.value.caption,
        visibility = _uiState.value.visibility,
        placeName = _uiState.value.selectedPlace!!.name,
        placeAddress = _uiState.value.selectedPlace!!.address,
        placeLatitude = _uiState.value.selectedPlace!!.latitude,
        placeLongitude = _uiState.value.selectedPlace!!.longitude,
      )

      if (res.first) {
        _uiState.update { currentState ->
          currentState.copy(
            loadingState = LoadingStates.SUCCESS
          )
        }
        _uiState.value.snackBarHostState.showSnackbar("Posted successfully")
      } else {
        _uiState.update { currentState ->
          currentState.copy(
            loadingState = LoadingStates.ERROR
          )
        }
        _uiState.value.snackBarHostState.showSnackbar(res.second)
      }
    }
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

  fun onVisibilityChange(visibility: String) {
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
        inputValid = currentState.captionValid && currentState.imageURL != null && currentState.selectedPlace != null && currentState.visibility.isNotBlank()
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
    performUpdateAutocompleteResult()
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
        placesAutocompleteRepository.getPlaceAutocompleteResultsDebounced(
          _uiState.value.placeSearchQuery
        ) { res ->
          _uiState.update { currentState ->
            currentState.copy(placeSearchResult = res.map { it.asDomainModel() })
          }
        }
      }
    }
  }
  //endregion
}

data class NewPostDetailsUiState(
  // Input
  val caption: String = "",
  val visibility: String = PostVisibilities.PUBLIC.name,

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
  val loadingState: LoadingStates = LoadingStates.IDLE,

  // Snackbar
  val snackBarHostState: SnackbarHostState = SnackbarHostState(),
)
