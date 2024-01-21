package com.oreomrone.locallens.ui.tests

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CameraTestViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(CameraTestUiState())
  val uiState: StateFlow<CameraTestUiState> = _uiState.asStateFlow()

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
  }

  fun onImageRemove() {
    _uiState.update { currentState ->
      currentState.copy(imageURL = null, imageFile = null)
    }
  }
  //endregion
}

data class CameraTestUiState(
  // Image
  val imageURL: String? = null,
  val imageFile: ByteArray? = null,
  )
