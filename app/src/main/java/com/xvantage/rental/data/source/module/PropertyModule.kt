package com.xvantage.rental.data.source.module

import com.xvantage.rental.data.remote.APIInterface
import com.xvantage.rental.data.source.PropertyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  06/05/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

@Module
@InstallIn(SingletonComponent::class)
object PropertyModule {

    @Provides
    @Singleton
    fun providePropertyRepository(apiInterface: APIInterface): PropertyRepository {
        return PropertyRepository(apiInterface)
    }
}