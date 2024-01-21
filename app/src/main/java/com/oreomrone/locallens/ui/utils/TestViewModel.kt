package com.oreomrone.locallens.ui.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
  private val profileRepository: ProfileRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(TestUiState())
  val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

  init {
//    viewModelScope.launch {
//      val res = profileRepository.updateProfile(
//        id = "8b0c4a1d-3633-4f3b-a336-dc4bf69f88f9",
//        fullName = "Quan Do",
//        username = "quanhoangdo",
//        bio = "I love taking pictures",
//        isPrivate = true,
//        imageUrl = "https://images.unsplash.com/photo-1564564321837-a57b7070ac4f?q=80&w=2376&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
//      )
//      Log.d(
//        "TestViewModel",
//        "init: ${res.first} ${res.second}"
//      )
//    }
  }
}

data class TestUiState(
  val test: String = ""
)
