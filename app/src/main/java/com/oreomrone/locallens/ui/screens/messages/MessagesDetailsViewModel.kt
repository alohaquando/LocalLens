package com.oreomrone.locallens.ui.screens.messages

import MessageThread
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.domain.LoadingStates
import com.oreomrone.locallens.domain.User
import com.oreomrone.locallens.ui.utils.SampleData
import com.oreomrone.locallens.ui.utils.getCurrentUser
import com.oreomrone.locallens.ui.utils.getRecipientFromThreadParticipantsPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagesDetailsViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle
) : ViewModel() {
  private val _uiState = MutableStateFlow(MessagesDetailsUiState())
  val uiState: StateFlow<MessagesDetailsUiState> = _uiState.asStateFlow()

  val id = savedStateHandle.get<String>("id")

  init {
    viewModelScope.launch {
      initializeUiState()
    }
  }

  private suspend fun initializeUiState() {
//    if (id === null) {
//      logAndSetError("id is null")
//      return
//    }

    viewModelScope.launch {
//      val currentUser = getCurrentUser()
//      if (currentUser === null) {
//        logAndSetError("currentUser is null")
//        return@launch
//      }

      val thread = SampleData.sampleThread
      if (thread === null) {
        logAndSetError("thread is null")
        return@launch
      }

      _uiState.update { currentState ->
        currentState.copy(
          currentUser = SampleData.sampleUser1,
          thread = thread,
          recipient = getRecipientFromThreadParticipantsPair(
            participants = thread.participants,
            currentUser = SampleData.sampleUser1,
          ),
          loadingState =  LoadingStates.SUCCESS
        )
      }
    }
  }

  private suspend fun getThread(id: String): MessageThread? {
    val thread = null // TODO
    // Note: the messages in the UI is displayed in reverse order, so sort the messages if needed
    return thread
  }

  fun onDraftMessageChange(draftMessage: String) {
    _uiState.update { currentState ->
      currentState.copy(draftMessage = draftMessage)
    }
  }

  suspend fun performSendMessage() {
    _uiState.update { currentState ->
      currentState.copy(draftMessage = "")
    }
    // TODO
  }

  private fun logAndSetError(message: String) {
    Log.e(
      "MessagesDetailsViewModel",
      message
    )
    _uiState.update { currentState ->
      currentState.copy(
        loadingState = LoadingStates.ERROR
      )
    }
  }
}

data class MessagesDetailsUiState(
  val thread: MessageThread? = null,
  val recipient: User? = null,
  val currentUser: User? = null,
  val draftMessage: String = "",
  val loadingState: LoadingStates = LoadingStates.LOADING
)
