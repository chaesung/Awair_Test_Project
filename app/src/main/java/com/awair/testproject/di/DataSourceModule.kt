package com.awair.testproject.di

import com.awair.testproject.event.list.data.EventListDataSource
import com.awair.testproject.event.list.data.EventListDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun providesEventListDataSource(source: EventListDataSourceImpl): EventListDataSource {
        return source
    }
}