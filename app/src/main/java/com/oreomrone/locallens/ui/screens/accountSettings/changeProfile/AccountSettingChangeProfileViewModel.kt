package com.oreomrone.locallens.ui.screens.accountSettings.changeProfile

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.utils.getCurrentUser
import com.oreomrone.locallens.ui.utils.validateBio
import com.oreomrone.locallens.ui.utils.validateName
import com.oreomrone.locallens.ui.utils.validateUsername
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountSettingChangeProfileViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  private val _uiState = MutableStateFlow(AccountSettingChangeProfileUiState())
  val uiState: StateFlow<AccountSettingChangeProfileUiState> = _uiState.asStateFlow()

  //region Init & Database calls
  init {
    viewModelScope.launch {
      initializeUiState()
    }
  }

  private suspend fun initializeUiState() {
    viewModelScope.launch {
      val currentUser = getCurrentUser()

      if (currentUser == null) {
        logAndSetError("currentUser is null")
        return@launch
      }

      _uiState.update { currentState ->
        currentState.copy(
          name = currentUser.name,
          username = currentUser.username,
          email = currentUser.email,
          bio = currentUser.bio,
          imageURL = currentUser.image,
          loadingState = LoadingStates.SUCCESS
        )
      }
    }
  }

  private fun logAndSetError(message: String) {
    Log.e(
      "AccountSettingChangeProfileViewModel",
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

data class AccountSettingChangeProfileUiState(
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
  val loadingState: LoadingStates = LoadingStates.LOADING,

  // Snackbar
  val snackbarHostState: SnackbarHostState = SnackbarHostState()
)
