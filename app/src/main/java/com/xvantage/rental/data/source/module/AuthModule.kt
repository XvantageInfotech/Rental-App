package com.xvantage.rental.data.source.module

import com.xvantage.rental.data.remote.APIClient
import com.xvantage.rental.data.remote.APIInterface
import com.xvantage.rental.data.source.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    @Named("open")
    fun provideOpenApiAuthService(retrofitBuilder: Retrofit.Builder): APIInterface {
        return retrofitBuilder
            .build()
            .create(APIInterface::class.java)
    }
    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://your-base-url.com/") 
    }
    @Singleton
    @Provides
    @Named("default")
    fun provideApiInterface(): APIInterface {
        return APIClient.appInterfaceServerUser()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(@Named("open") apiInterface: APIInterface): AuthRepository {
        return AuthRepository(apiInterface)
    }
}
