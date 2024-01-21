package com.oreomrone.locallens.ui.screens.messages

import MessageThread
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.domain.User
import com.oreomrone.locallens.ui.utils.getCurrentUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagesListViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(MessagesListUiState())
  val uiState: StateFlow<MessagesListUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      // 1. Get current user
      val currentUser = getCurrentUser()

      if (currentUser == null) {
        logAndSetError("currentUser is null")
        return@launch
      }

      _uiState.update { currentState ->
        currentState.copy(currentUser = currentUser)
      }
      getThreads(_uiState.value.currentUser!!.id)
    }
  }

  private suspend fun getThreads(userId: String) {
    var threads = emptyList<MessageThread>() // TODO
    threads = sortThreads(threads)
    _uiState.update { currentState ->
      currentState.copy(threads = threads)
    }
  }

  private suspend fun sortThreads(threads: List<MessageThread>): List<MessageThread> {
    return threads.sortedByDescending { it.lastUpdated }.sortedByDescending { it.hasUnreadMessage }
  }

  private fun logAndSetError(message: String) {
    Log.e(
      "MessagesListViewModel",
      message
    )
    _uiState.update { currentState ->
      currentState.copy(isError = true)
    }
  }
}

data class MessagesListUiState(
  val threads: List<MessageThread> = emptyList(),
  val currentUser: User? = null, // TODO
  val isLoading: Boolean = false,
  val isError: Boolean = false,
)

