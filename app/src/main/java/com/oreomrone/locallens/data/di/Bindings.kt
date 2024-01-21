@file:Suppress("unused")

package com.oreomrone.locallens.data.di

import com.oreomrone.locallens.data.repositories.placesAutocomplete.PlacesAutocompleteRepository
import com.oreomrone.locallens.data.repositories.placesAutocomplete.PlacesAutocompleteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class Bindings {
    @Binds
    abstract fun bindPlacesAutocompleteRepository(impl: PlacesAutocompleteRepositoryImpl): PlacesAutocompleteRepository
}