package com.xvantage.rental.di

import com.xvantage.rental.data.PropertyRepository
import com.xvantage.rental.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePropertyRepository(apiService: ApiService): PropertyRepository {
        return PropertyRepository(apiService)
    }
}