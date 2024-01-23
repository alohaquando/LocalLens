package com.oreomrone.locallens.ui.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.oreomrone.locallens.data.repositories.post.PostRepository
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.domain.User
import com.oreomrone.locallens.domain.dtoToDomain.toPost
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
  private val postRepository: PostRepository,
  private val profileRepository: ProfileRepository,
  private val auth: Auth,
) : ViewModel() {
  private val _uiState = MutableStateFlow(PostUiState())
  val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

  suspend fun getPost(id: String) {
    val post = postRepository.getPost(id)?.toPost()

    if (post != null) {
      _uiState.update { currentState ->
        currentState.copy(
          post = post,
          place = post.place.name,
          address = post.place.address,
          caption = post.caption,
          username = post.user?.username ?: "",
          date = jsonTimestampToDateTime(post.timestamp),
          postImageModel = post.image,
          userImageModel = post.user?.image ?: "",
          favorites = post.favorites,
          showMenuButton = false
        )
      }

      val currentUserId = auth.currentUserOrNull()?.id

      if (post.favorites.isEmpty()) {
        _uiState.update { currentState ->
          currentState.copy(
            isFavorite = false
          )
        }
      }

      if (post.user?.id == currentUserId) {
        _uiState.update { currentState ->
          currentState.copy(
            showMenuButton = true
          )
        }
      }

      for (user in post.favorites) {
        if (user.id == currentUserId) {
          _uiState.update { currentState ->
            currentState.copy(
              isFavorite = true
            )
          }
          break
        } else {
          _uiState.update { currentState ->
            currentState.copy(
              isFavorite = false
            )
          }
        }
      }
    }
  }

  suspend fun performPromotePost(id: String) {
    // TODO
  }

  suspend fun performDeletePost(id: String) {
    // TODO
  }

  suspend fun performFavoritePost(id: String) {
    _uiState.update { currentState ->
      currentState.copy(
        favoriteLoadingState = LoadingStates.LOADING
      )
    }
    postRepository.toggleFavorite(id)
    getPost(id)
    _uiState.update { currentState ->
      currentState.copy(
        favoriteLoadingState = LoadingStates.SUCCESS
      )
    }
  }

  fun performNavigate(
    lat: Double,
    long: Double,
    name: String,
    context: Context,
    packageManager: PackageManager
  ) {
    val gmmIntentUri = Uri.parse("geo:${lat},${long}?q=${lat},${long}(${name})")
    val mapIntent = Intent(
      Intent.ACTION_VIEW,
      gmmIntentUri
    )
    mapIntent.setPackage("com.google.android.apps.maps")
    mapIntent.resolveActivity(packageManager)?.let {
      ContextCompat.startActivity(
        context,
        mapIntent,
        null
      )
    }
  }

  fun performNavigate(
    context: Context,
    packageManager: PackageManager
  ) {
    val gmmIntentUri = Uri.parse(
      "geo:${_uiState.value.post?.place?.latitude},${
        _uiState.value.post?.place?.longitude
      }?q=${_uiState.value.post?.place?.latitude}," + "${_uiState.value.post?.place?.longitude}" + "(${_uiState.value.place})"
    )
    val mapIntent = Intent(
      Intent.ACTION_VIEW,
      gmmIntentUri
    )
    mapIntent.setPackage("com.google.android.apps.maps")
    mapIntent.resolveActivity(packageManager)?.let {
      ContextCompat.startActivity(
        context,
        mapIntent,
        null
      )
    }
  }
}


data class PostUiState(
  val post: Post? = null,
  val place: String = "Loading...",
  val address: String = "Loading...",
  val caption: String = "Loading...",
  val username: String = "Loading...",
  val date: String = "Loading...",
  val postImageModel: Any = "",
  val userImageModel: Any = "",
  val favorites: List<User> = emptyList(),
  val isFavorite: Boolean = false,
  val showMenuButton: Boolean = false,
  val favoriteLoadingState: LoadingStates = LoadingStates.IDLE
)