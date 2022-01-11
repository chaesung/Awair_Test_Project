package com.awair.testproject.event.list.data

import com.awair.testproject.event.list.data.entity.EventList
import com.awair.testproject.network.api.EventApiService
import retrofit2.Response
import javax.inject.Inject

class EventListDataSourceImpl @Inject constructor(private val eventApiService: EventApiService) :
    EventListDataSource {
    override suspend fun getEvents(): Response<EventList> {
        return eventApiService.getEvents()
    }

    override suspend fun getNextEvents(nextPageToken: String): Response<EventList> {
        return eventApiService.getEvents(nextPageToken)
    }

}