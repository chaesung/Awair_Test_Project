package com.awair.testproject.event.list.repository

import com.awair.testproject.event.list.data.entity.EventList
import com.awair.testproject.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface EventListRepository {
    suspend fun getEvents(): Flow<NetworkResult<EventList>>
    suspend fun getNextEvents(nextPageToken: String): Flow<NetworkResult<EventList>>
}