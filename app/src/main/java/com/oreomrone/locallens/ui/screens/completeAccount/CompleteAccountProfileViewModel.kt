package com.oreomrone.locallens.ui.screens.completeAccount

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.auth.AuthRepository
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.utils.validateBio
import com.oreomrone.locallens.ui.utils.validateName
import com.oreomrone.locallens.ui.utils.validateUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteAccountProfileViewModel @Inject constructor(
  private val auth: Auth,
  private val authRepository: AuthRepository,
  private val profileRepository: ProfileRepository,
) : ViewModel() {
  private val _uiState = MutableStateFlow(CompleteAccountProfileUiState())
  val uiState: StateFlow<CompleteAccountProfileUiState> = _uiState.asStateFlow()

  //region Init & Database calls
  init {
    viewModelScope.launch {
      initializeUiState()
    }
  }

  private suspend fun initializeUiState() {
    auth.currentSessionOrNull()?.user?.let {
      val profile = profileRepository.getProfileById(it.id)
      if (profile !== null) {
        _uiState.update { currentState ->
          currentState.copy(
            name = profile.fullName,
            username = profile.username,
            email = profile.email,
            bio = profile.bio,
            imageURL = profile.image,
            loadingStates = LoadingStates.SUCCESS
          )
        }
      }
      else {
        _uiState.update { currentState ->
          currentState.copy(
            loadingStates = LoadingStates.ERROR
          )
        }
      }
    }
  }

  suspend fun performComplete() {
    profileRepository
  }

  suspend fun performSignOut() {
    viewModelScope.launch {
      authRepository.signOut()
    }
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
  val loadingStates: LoadingStates = LoadingStates.LOADING,

  // Snackbar
  val snackbarHostState: SnackbarHostState = SnackbarHostState()
)
