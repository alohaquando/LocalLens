package com.oreomrone.locallens.data.repositories.placesAutocomplete

import com.oreomrone.locallens.data.dto.PlaceAutocomplete


interface PlacesAutocompleteRepository {
    suspend fun getPlaceAutocompleteResults(query: String): List<PlaceAutocomplete>
}