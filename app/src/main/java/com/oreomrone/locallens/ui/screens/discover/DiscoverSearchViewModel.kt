package com.oreomrone.locallens.ui.screens.discover

import androidx.lifecycle.ViewModel
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.User
import com.oreomrone.locallens.ui.screens.auth.AuthSignInInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class DiscoverSearchViewModel @Inject constructor(

) : ViewModel() {
  private val _uiState = MutableStateFlow(DiscoverSearchUiState())
  val uiState: StateFlow<DiscoverSearchUiState> = _uiState.asStateFlow()

  fun onSearchQueryChange(query: String) {
    _uiState.update { currentState ->
      currentState.copy(query = query)
    }
  }

  fun onFilterChange(filter: DiscoverSearchFilter) {
    _uiState.update { currentState ->
      currentState.copy(selectedFilter = filter)
    }
  }


  // TODO: Return data function
}

data class DiscoverSearchUiState(
  val query: String = "",
  val resultPlaces: List<Place> = listOf(),
  val resultUsers: List<User> = listOf(),
  val selectedFilter: DiscoverSearchFilter = DiscoverSearchFilter.ALL,
  val isLoading: Boolean = false,
)

enum class DiscoverSearchFilter {
  ALL,
  PLACES,
  PEOPLE,
}