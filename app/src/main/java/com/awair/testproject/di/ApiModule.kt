package com.awair.testproject.di

import com.awair.testproject.network.ApiConstants
import com.awair.testproject.network.AwairRetrofitClient
import com.awair.testproject.network.api.EventApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val DEBUG = true

    @Provides
    @Singleton
    fun provideEventApiService(): EventApiService {
        return AwairRetrofitClient.createNetworkClient(ApiConstants.BASE_URL, DEBUG)
    }
}