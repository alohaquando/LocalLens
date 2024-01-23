package com.oreomrone.locallens.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oreomrone.locallens.data.repositories.place.PlaceRepository
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.User
import com.oreomrone.locallens.domain.dtoToDomain.toPlace
import com.oreomrone.locallens.domain.dtoToDomain.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverSearchViewModel @Inject constructor(
  private val profileRepository: ProfileRepository,
  private val placeRepository: PlaceRepository
) : ViewModel() {
  private val _uiState = MutableStateFlow(DiscoverSearchUiState())
  val uiState: StateFlow<DiscoverSearchUiState> = _uiState.asStateFlow()

  suspend fun onSearchQueryChange(query: String) {
    viewModelScope.launch {
      _uiState.update { currentState ->
        currentState.copy(query = query)
      }
      performSearch()
    }
  }

  private suspend fun performSearch() {
    viewModelScope.launch {
      val resultPlaces = placeRepository.getPlacesByName(uiState.value.query).map { it.toPlace() }
      val resultUsers =
        profileRepository.getProfilesByUsername(uiState.value.query).map { it.toUser() }

      _uiState.update { currentState ->
        currentState.copy(
          resultPlaces = resultPlaces,
          resultUsers = resultUsers
        )
      }
    }
  }

  fun onFilterChange(filter: DiscoverSearchFilter) {
    _uiState.update { currentState ->
      currentState.copy(selectedFilter = filter)
    }
  }
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