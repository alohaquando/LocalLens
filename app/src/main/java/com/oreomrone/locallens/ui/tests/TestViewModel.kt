package com.oreomrone.locallens.ui.tests

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.post.PostRepository
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
  private val profileRepository: ProfileRepository,
  private val postRepository: PostRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(TestUiState())
  val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
//      postRepository.getPostsByPlaceId("f7e2dfbc-bc77-4acf-82fb-8e14b412c395")
    }
  }
}

data class TestUiState(
  val test: String = ""
)
