package com.oreomrone.locallens.ui.screens.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.ui.utils.PostClusterItem
import com.oreomrone.locallens.ui.utils.SampleData.samplePost
import com.oreomrone.locallens.ui.utils.SampleData.samplePost1
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostsViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(PostsUiState())
  val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

  init {
    getPosts()
  }

  private fun getPosts() {
    viewModelScope.launch {
      val posts = listOf(
        samplePost,
        samplePost1
      ) // TODO: Get new post
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
          loadingState = LoadingStates.SUCCESS
        )
      }
    }
  }
}

data class PostsUiState(
  val posts: List<Post> = listOf(),
  val startingMapPosition: LatLng = LatLng(
    10.777263208853345,
    106.69534102887356
  ), // TODO: Replace with device location
  val postsClusterItems: List<PostClusterItem> = emptyList(),
  val loadingState: LoadingStates = LoadingStates.LOADING
)
