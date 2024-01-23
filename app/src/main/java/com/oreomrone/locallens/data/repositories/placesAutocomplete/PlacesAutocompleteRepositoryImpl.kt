package com.oreomrone.locallens.data.repositories.placesAutocomplete

import android.util.Log
import com.oreomrone.locallens.data.dto.PlaceAutocompleteDto
import com.oreomrone.locallens.data.dto.PlacesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PlacesAutocompleteRepositoryImpl @Inject constructor(
  private val httpClient: HttpClient
) : PlacesAutocompleteRepository {
  private val debounceTime = 200L // milliseconds
  private var searchJob: Job? = null

  // CoroutineScope for handling coroutine cancellation
  private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

  // Function to debounce
  override suspend fun getPlaceAutocompleteResultsDebounced(
    query: String,
    onResults: (List<PlaceAutocompleteDto>) -> Unit
  ) {
    // Cancel the previous job if it exists
    searchJob?.cancel()

    // Create a new job for the current invocation
    searchJob = coroutineScope.launch {
      // Delay for the specified debounce time
      delay(debounceTime)

      // Actual function call after the delay
      val results = getPlaceAutocompleteResults(query)

      // Handle the results using the provided lambda
      onResults(results)
    }

    // Wait for the job to complete (if it hasn't been cancelled)
    searchJob?.join()
  }


  @Suppress("SpellCheckingInspection")
  override suspend fun getPlaceAutocompleteResults(query: String): List<PlaceAutocompleteDto> =
    withContext(Dispatchers.IO) {
      Thread.sleep(300)

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

      Log.i(
        "PlacesAutocomplete",
        response.bodyAsText()
      )

      return@withContext if (response.bodyAsText()
          .isBlank() || response.bodyAsText() == "{}" || !response.bodyAsText().contains("places")
      ) {
        emptyList()
      } else {
        json.decodeFromString<PlacesResponseDto>(response.bodyAsText()).placesDto
      }
    }
}