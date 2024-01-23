package com.oreomrone.locallens.ui.screens.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.post.PostRepository
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.domain.User
import com.oreomrone.locallens.domain.dtoToDomain.toPost
import com.oreomrone.locallens.domain.dtoToDomain.toUser
import com.oreomrone.locallens.ui.utils.SampleData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsPersonViewModel @Inject constructor(
  private val profileRepository: ProfileRepository,
  private val postRepository: PostRepository,
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  private val _uiState = MutableStateFlow(DetailsPersonUiState())
  val uiState: StateFlow<DetailsPersonUiState> = _uiState.asStateFlow()

  val id = savedStateHandle.get<String>("id")

  init {
    viewModelScope.launch { initializeUiState() }
  }

   suspend fun initializeUiState() {
    if (id.isNullOrEmpty()) {
      logAndSetError("id is null or empty")
      return
    }

    viewModelScope.launch {
      val user = getUser(id)

      if (user === null) {
        logAndSetError("user is null")
        return@launch
      }

      val posts = getPostsByUserId(id);
      user.posts = posts

      _uiState.update { currentState ->
        currentState.copy(
          user = user,
          loadingStates = LoadingStates.SUCCESS
        )
      }
    }
  }

  private suspend fun getUser(id: String) : User? {
    return profileRepository.getProfileById(id)?.toUser()
  }

  private suspend fun getPostsByUserId(id: String): List<Post> {
    return postRepository.getPostsByUserId(id).map { it.toPost() }
  }

  private fun logAndSetError(message: String) {
    Log.e(
      "DetailsPersonViewModel",
      message
    )
    _uiState.update { currentState ->
      currentState.copy(
        loadingStates = LoadingStates.ERROR
      )
    }
  }
}

data class DetailsPersonUiState(
  val user: User? = null,
  val loadingStates: LoadingStates = LoadingStates.LOADING
)
