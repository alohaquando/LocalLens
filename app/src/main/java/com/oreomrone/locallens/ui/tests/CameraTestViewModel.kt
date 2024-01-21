package com.oreomrone.locallens.ui.tests

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CameraTestViewModel @Inject constructor(
  val supabaseClient: SupabaseClient,
  val auth: Auth
) : ViewModel() {
  private val _uiState = MutableStateFlow(CameraTestUiState())
  val uiState: StateFlow<CameraTestUiState> = _uiState.asStateFlow()
}

data class CameraTestUiState(
  // Image
  val imageURL: String? = null,
  val imageFile: ByteArray? = null,
)
