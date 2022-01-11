package com.awair.testproject.event.list.repository

import com.awair.testproject.event.list.data.entity.EventList
import com.awair.testproject.event.list.data.EventListDataSource
import com.awair.testproject.network.BaseApiResponse
import com.awair.testproject.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EventListRepositoryImpl @Inject constructor(private val mainDataSource: EventListDataSource): BaseApiResponse(), EventListRepository {
    /**
     * Gets first page of the event list
     */
    override suspend fun getEvents(): Flow<NetworkResult<EventList>> {
        return flow { emit(safeApiCall { mainDataSource.getEvents() }) }
            .flowOn(Dispatchers.IO)
    }

    /**
     * Gets next page of the event list
     * using next page token from previous api call
     */
    override suspend fun getNextEvents(nextPageToken: String): Flow<NetworkResult<EventList>> {
        return flow { emit(safeApiCall { mainDataSource.getNextEvents(nextPageToken) }) }
            .flowOn(Dispatchers.IO)
    }
}