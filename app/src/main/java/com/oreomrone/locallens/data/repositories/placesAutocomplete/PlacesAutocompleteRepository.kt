package com.oreomrone.locallens.data.repositories.placesAutocomplete

import com.oreomrone.locallens.data.dto.PlaceAutocompleteDto


interface PlacesAutocompleteRepository {
    suspend fun getPlaceAutocompleteResults(query: String): List<PlaceAutocompleteDto>
  suspend fun getPlaceAutocompleteResultsDebounced(
    query: String,
    onResults: (List<PlaceAutocompleteDto>) -> Unit
  )
}