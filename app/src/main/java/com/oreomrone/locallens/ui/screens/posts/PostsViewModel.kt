package com.oreomrone.locallens.ui.screens.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.oreomrone.locallens.data.repositories.post.PostRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.domain.dtoToDomain.toPost
import com.oreomrone.locallens.ui.utils.PostClusterItem
import com.oreomrone.locallens.ui.utils.SampleData.samplePost
import com.oreomrone.locallens.ui.utils.SampleData.samplePost1
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
  private val postRepository: PostRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(PostsUiState())
  val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

  init {
    getPosts()
  }

  fun getPosts() {
    viewModelScope.launch {
      val posts = postRepository.getAllPost().map { it.toPost() }
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
