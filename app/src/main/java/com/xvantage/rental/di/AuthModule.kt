package com.xvantage.rental.di

import com.xvantage.rental.data.remote.APIInterface
import com.xvantage.rental.data.source.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(apiInterface: APIInterface): AuthRepository {
        return AuthRepository(apiInterface)
    }
}