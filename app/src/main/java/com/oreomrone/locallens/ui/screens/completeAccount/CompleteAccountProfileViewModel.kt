package com.oreomrone.locallens.ui.screens.completeAccount

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.ui.utils.validateBio
import com.oreomrone.locallens.ui.utils.validateName
import com.oreomrone.locallens.ui.utils.validateUsername
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompleteAccountProfileViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(CompleteAccountProfileUiState())
  val uiState: StateFlow<CompleteAccountProfileUiState> = _uiState.asStateFlow()

  //region Init & Database calls
  init {
    viewModelScope.launch {
      getUserInfo()
    }
  }

  private suspend fun getUserInfo() {
    // TODO
  }

  suspend fun performComplete() {
    // TODO
  }

  suspend fun performSignOut() {
    // TODO
  }
  //endregion

  fun onUsernameChange(username: String) {
    _uiState.update { currentState ->
      currentState.copy(
        username = username,
        usernameValid = validateUsername(username)
      )
    }
    validateInput()
  }


  fun onNameChange(name: String) {
    _uiState.update { currentState ->
      currentState.copy(
        name = name,
        nameValid = validateName(name)
      )
    }
    validateInput()
  }

  fun onBioChange(bio: String) {
    _uiState.update { currentState ->
      currentState.copy(
        bio = bio,
        bioValid = validateBio(bio)
      )
    }
    validateInput()
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
        inputValid = currentState.nameValid && currentState.usernameValid && currentState.bioValid
      )
    }
  }
}

data class CompleteAccountProfileUiState(
  // Input
  val name: String = "",
  val username: String = "",
  val email: String = "", // Take from auth
  val bio: String = "",

  // Image
  val imageURL: String? = null,
  val imageFile: ByteArray? = null,

  // Validation
  val nameValid: Boolean = false,
  val usernameValid: Boolean = false,
  val bioValid: Boolean = false,
  val inputValid: Boolean = false,

  // States
  val isSuccess: Boolean = false,
  val isError: Boolean = false,
  val isLoading: Boolean = false,

  // Snackbar
  val snackbarHostState: SnackbarHostState = SnackbarHostState()
)
