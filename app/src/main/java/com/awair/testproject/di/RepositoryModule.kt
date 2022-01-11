package com.awair.testproject.di

import com.awair.testproject.event.list.repository.EventListRepository
import com.awair.testproject.event.list.repository.EventListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesEventListRepository(repository: EventListRepositoryImpl): EventListRepository {
        return repository
    }
}