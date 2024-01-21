package com.oreomrone.locallens.ui.screens.details.map

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.ui.utils.PostClusterItem
import com.oreomrone.locallens.ui.utils.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsPostsMapViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  private val _uiState = MutableStateFlow(DetailsPostsMapUiState())
  val uiState: StateFlow<DetailsPostsMapUiState> = _uiState.asStateFlow()

  val id = savedStateHandle.get<String>("id")

  init {
    viewModelScope.launch {
      initializeUiState()
    }
  }

  private suspend fun initializeUiState() {
    if (id === null) {
      logAndSetError("id is null")
      return
    }

    viewModelScope.launch {
      getPosts(userId = id)
    }
  }

  private fun getPosts(userId: String) {
    viewModelScope.launch {
      val posts = _uiState.value.posts // TODO: Get user's post
      val postsClusterItems = posts.map {
        PostClusterItem(
          itemPosition = LatLng(
            it.place.latitude,
            it.place.longitude
          ),
          itemTitle = "",
          itemSnippet = "",
          itemZIndex = 1f,
          post = it
        )
      }
      _uiState.update { currentState ->
        currentState.copy(
          posts = posts,
          postsClusterItems = postsClusterItems,
          loadingState = LoadingStates.SUCCESS,
        )
      }
    }
  }

  private fun logAndSetError(message: String) {
    Log.e(
      "DetailsPostsMapViewModel",
      message
    )
    _uiState.update { currentState ->
      currentState.copy(
        loadingState = LoadingStates.ERROR
      )
    }
  }
}

data class DetailsPostsMapUiState(
  val posts: List<Post> = listOf(
    SampleData.samplePost1,
    SampleData.samplePost
  ),
  val startingMapPosition: LatLng = LatLng(
    10.777263208853345,
    106.69534102887356
  ), // TODO: Replace with device location
  val postsClusterItems: List<PostClusterItem> = emptyList(),
  val loadingState: LoadingStates = LoadingStates.LOADING,
)
