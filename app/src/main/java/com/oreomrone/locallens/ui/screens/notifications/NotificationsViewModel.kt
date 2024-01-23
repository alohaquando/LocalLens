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
  val notifications: List<AppNotification> = listOf(
    AppNotification(
      id = "001",
      image = "https://hyjllqwtvlxqtoxcgbkf.supabase.co/storage/v1/object/public/profiles/9baa5746-f7a6-4d31-bb17-b24ee3cf3578.png",
      content = "This is a sample notification",
      timestamp = "2024-01-22T13:03:58.590832+00:00",
    ),
    AppNotification(
      id = "001",
      image = "https://images.unsplash.com/photo-1595032332002-a062b99ce108?q=80&w=3387&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
      content = "@sheen_hahn_athan liked your post",
      timestamp = "2024-01-22T13:03:58.590832+00:00",
    ),
    AppNotification(
      id = "001",
      image = "https://images.unsplash.com/photo-1595032332002-a062b99ce108?q=80&w=3387&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
      content = "@sheen_hahn_athan requested to follow you",
      timestamp = "2024-01-22T13:03:58.590832+00:00",
    ),
  )
)