package com.oreomrone.locallens.ui.screens.notifications

import androidx.lifecycle.ViewModel
import com.oreomrone.locallens.domain.AppNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NotificationsViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(NotificationsUiState())
  val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

  // TODO
}

data class NotificationsUiState(
  val notifications: List<AppNotification> = emptyList()
)