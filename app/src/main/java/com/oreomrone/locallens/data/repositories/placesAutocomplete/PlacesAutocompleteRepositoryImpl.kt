package com.oreomrone.locallens.data.repositories.placesAutocomplete

import android.util.Log
import com.oreomrone.locallens.data.dto.PlaceAutocomplete
import com.oreomrone.locallens.data.dto.PlacesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PlacesAutocompleteRepositoryImpl @Inject constructor(
  private val httpClient: HttpClient
) : PlacesAutocompleteRepository {

  @Suppress("SpellCheckingInspection")
  override suspend fun getPlaceAutocompleteResults(query: String): List<PlaceAutocomplete> {
    val response = httpClient.post("https://places.googleapis.com/v1/places:searchText") {
      headers {
        append(
          HttpHeaders.ContentType,
          ContentType.Application.Json
        )
        append(
          "X-Goog-Api-Key",
          "AIzaSyD_RF-XX9buzSPGjJyiSMLUMFoFfMeYs_4"
        )
        append(
          "X-Goog-Fieldmask",
          "places.displayName,places.formattedAddress,places.location,places.displayName,places.id"
        )
      }
      setBody(
        """
                {
                  "textQuery" : "${query}}",
                  "locationBias": {
                  "rectangle": {
                    "low": {
                      "latitude": 8.18,
                      "longitude": 102.14
                    },
                    "high": {
                      "latitude": 23.39,
                      "longitude": 109.46
                    }
                  }
                }
                }
            """.trimIndent()
      )

    }

    val json = Json { ignoreUnknownKeys = true }

        Log.i("PlacesAutocomplete", response.bodyAsText())

    return if (response.bodyAsText()
        .isBlank() || response.bodyAsText() == "{}" || !response.bodyAsText().contains("places")
    ) {
      emptyList()
    } else {
      json.decodeFromString<PlacesResponse>(response.bodyAsText()).places
    }
  }
}