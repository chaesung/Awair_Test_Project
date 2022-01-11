package com.awair.testproject.event.list.data

import com.awair.testproject.event.list.data.entity.EventList
import retrofit2.Response

interface EventListDataSource {
    suspend fun getEvents(): Response<EventList>
    suspend fun getNextEvents(nextPageToken: String): Response<EventList>
}