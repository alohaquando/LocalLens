package com.oreomrone.locallens.ui.screens.accountSettings.support

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SupportViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(SupportUiState())
  val uiState: StateFlow<SupportUiState> = _uiState.asStateFlow()

  fun onTypeSelected(type: SupportType) {
    _uiState.value = _uiState.value.copy(type = type)
    validateInput()
  }

  fun onContentChanged(content: String) {
    _uiState.value = _uiState.value.copy(content = content)
    validateInput()
  }

  fun performSubmit() {
    // TODO
  }

  private fun validateInput() {
    _uiState.value = _uiState.value.copy(
      isInputValid = _uiState.value.content.isNotBlank() && _uiState.value.type != null
    )
  }
}

data class SupportUiState(
  val type: SupportType? = null,
  val content: String = "",
  val isInputValid: Boolean = false,
)

enum class SupportType {
  QUESTION,
  FEEDBACK,
  BUG
}