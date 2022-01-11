package com.awair.testproject.network.api

import com.awair.testproject.event.list.data.entity.EventList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApiService {
    @GET("events")
    suspend fun getEvents(): Response<EventList>

    @GET("events")
    suspend fun getEvents(
        @Query("next_page_token") nextPageToken: String
    ): Response<EventList>
}