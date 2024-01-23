package com.oreomrone.locallens.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.post.PostRepository
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.domain.dtoToDomain.toPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
  private val postRepository: PostRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(DiscoverUiState())
  val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      initializeUiState()
    }
  }

  suspend fun initializeUiState() {
    viewModelScope.launch { val posts = postRepository.getAllPost().map { it.toPost() }

      _uiState.update { currentState ->
        currentState.copy(
          posts = posts,
          loadingStates = LoadingStates.SUCCESS
        )
      } }
  }

  suspend fun getPosts(): List<Post> {
    return listOf()// TODO
  }
}

data class DiscoverUiState(
  val posts: List<Post> = emptyList(),
  val loadingStates: LoadingStates = LoadingStates.LOADING
)
