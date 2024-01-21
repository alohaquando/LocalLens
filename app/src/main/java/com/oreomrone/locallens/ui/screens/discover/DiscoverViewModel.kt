package com.oreomrone.locallens.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscoverViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(DiscoverUiState())
  val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      initializeUiState()
    }
  }

  private suspend fun initializeUiState() {
    val posts = getPosts()

    _uiState.update { currentState ->
      currentState.copy(
        posts = posts,
        loadingStates = LoadingStates.SUCCESS
      )
    }
  }

  suspend fun getPosts(): List<Post> {
    return listOf()// TODO
  }
}

data class DiscoverUiState(
  val posts: List<Post> = emptyList(),
  val loadingStates: LoadingStates = LoadingStates.LOADING
)
