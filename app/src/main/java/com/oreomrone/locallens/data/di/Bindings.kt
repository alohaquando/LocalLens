@file:Suppress("unused")

package com.oreomrone.locallens.data.di

import com.oreomrone.locallens.data.repositories.auth.AuthRepository
import com.oreomrone.locallens.data.repositories.placesAutocomplete.PlacesAutocompleteRepository
import com.oreomrone.locallens.data.repositories.placesAutocomplete.PlacesAutocompleteRepositoryImpl
import com.oreomrone.locallens.data.repositories.auth.AuthRepositoryImpl
import com.oreomrone.locallens.data.repositories.place.PlaceRepository
import com.oreomrone.locallens.data.repositories.place.PlaceRepositoryImpl
import com.oreomrone.locallens.data.repositories.post.PostRepository
import com.oreomrone.locallens.data.repositories.post.PostRepositoryImpl
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import com.oreomrone.locallens.data.repositories.profile.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class Bindings {
    @Binds
    abstract fun bindPlacesAutocompleteRepository(impl: PlacesAutocompleteRepositoryImpl): PlacesAutocompleteRepository

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    abstract fun bindPlaceRepository(impl: PlaceRepositoryImpl): PlaceRepository

    @Binds
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository
}