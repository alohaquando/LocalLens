package com.oreomrone.locallens.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
  private val profileRepository: ProfileRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(TestUiState())
  val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      profileRepository.getAllProfile()
      profileRepository.getProfileById("8b0c4a1d-3633-4f3b-a336-dc4bf69f88f9")
      profileRepository.getProfileById("8b0c4a1d-3633-a336-dc4bf69f88f9")
    }
  }
}

data class TestUiState(
  val test: String = ""
)
