package com.oreomrone.locallens.ui.tests

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.place.PlaceRepository
import com.oreomrone.locallens.data.repositories.post.PostRepository
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.data.repositories.test.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
  private val testRepository: TestRepository,
  private val profileRepository: ProfileRepository,
  private val postRepository: PostRepository,
  private val placeRepository: PlaceRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(TestUiState())
  val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      testRepository.test()
    }
  }
}

data class TestUiState(
  val test: String = ""
)
