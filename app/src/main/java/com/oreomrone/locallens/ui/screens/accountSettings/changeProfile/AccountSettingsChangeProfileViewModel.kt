package com.oreomrone.locallens.ui.screens.accountSettings.changeProfile

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.auth.AuthRepository
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.ui.utils.validateBio
import com.oreomrone.locallens.ui.utils.validateName
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsChangeProfileViewModel @Inject constructor(
  private val auth: Auth,
  private val authRepository: AuthRepository,
  private val profileRepository: ProfileRepository,
) : ViewModel() {
  private val _uiState = MutableStateFlow(AccountSettingsChangeProfileUiState())
  val uiState: StateFlow<AccountSettingsChangeProfileUiState> = _uiState.asStateFlow()

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
        onBioChange(profile.bio)
        onNameChange(profile.fullName)
        onUsernameChange(profile.username)
        _uiState.update { currentState ->
          currentState.copy(
            imageURL = profile.image,
            isPrivate = profile.isPrivate,
            loadingState = LoadingStates.IDLE
          )
        }
        validateInput()
      } else {
        _uiState.update { currentState ->
          currentState.copy(
            loadingState = LoadingStates.ERROR
          )
        }
      }
    }
  }

  suspend fun performSave() {
    try {
      _uiState.update { currentState ->
        currentState.copy(
          loadingState = LoadingStates.LOADING
        )
      }

      val res = profileRepository.updateProfile(
        id = auth.currentSessionOrNull()!!.user!!.id,
        fullName = _uiState.value.name,
        username = _uiState.value.username,
        bio = _uiState.value.bio,
        isPrivate = _uiState.value.isPrivate,
        imageUrl = _uiState.value.imageURL,
        imageFile = _uiState.value.imageFile
      )

      when (res.first) {
        true -> {
          _uiState.update { currentState ->
            currentState.copy(
              loadingState = LoadingStates.SUCCESS,
            )
          }
          _uiState.value.snackbarHostState.showSnackbar(res.second)
        }

        else -> {
          _uiState.update { currentState ->
            currentState.copy(
              loadingState = LoadingStates.ERROR
            )
          }
        }
      }
    } catch (e: Exception) {
      _uiState.update { currentState ->
        currentState.copy(
          loadingState = LoadingStates.ERROR
        )
      }
    }
  }
  //endregion

  fun onUsernameChange(username: String) {
    _uiState.update { currentState ->
      currentState.copy(
        username = username,
      )
    }

    viewModelScope.launch {
      val res = profileRepository.validateUsernameUnique(username)
      _uiState.update { currentState ->
        currentState.copy(
          usernameValid = res
        )
      }
      validateInput()
    }
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

  fun onIsPrivateChange(isPrivate: Boolean) {
    _uiState.update { currentState ->
      currentState.copy(
        isPrivate = isPrivate
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
        inputValid = _uiState.value.nameValid && _uiState.value.bioValid && _uiState.value.usernameValid && !_uiState.value.imageURL.isNullOrEmpty()
      )
    }
  }
}

data class AccountSettingsChangeProfileUiState(
  // Input
  val name: String = "",
  val username: String = "",
  val email: String = "", // Take from auth
  val bio: String = "",
  val isPrivate: Boolean = false,

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
