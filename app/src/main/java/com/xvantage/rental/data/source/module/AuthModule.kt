package com.xvantage.rental.data.source.module

import com.xvantage.rental.data.remote.APIClient
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

    @Singleton
    @Provides
    fun provideApiInterface(): APIInterface {
        return APIClient.appInterfaceServerUser()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(apiInterface: APIInterface): AuthRepository {
        return AuthRepository(apiInterface)
    }
}

