package com.oreomrone.locallens.ui.tests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraTestViewModel @Inject constructor(
  val supabaseClient: SupabaseClient,
  val auth: Auth
) : ViewModel() {
  private val _uiState = MutableStateFlow(CameraTestUiState())
  val uiState: StateFlow<CameraTestUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      auth.sessionStatus.collect {
        val text = when (it) {
          is SessionStatus.Authenticated   -> it.session.user.toString()
          SessionStatus.LoadingFromStorage -> "Loading from storage"
          SessionStatus.NetworkError       -> "Network error"
          SessionStatus.NotAuthenticated   -> "Not authenticated"
        }

        _uiState.update { currentState ->
          currentState.copy(sessionText = text)
        }
      }
    }
  }
}

data class CameraTestUiState(
  // Image
  val imageURL: String? = null,
  val imageFile: ByteArray? = null,
  val sessionText: String = "",
)
