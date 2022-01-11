package com.awair.testproject.event.list.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class EventList(
    @Json(name = "events")
    val events: List<Event>,
    @Json(name = "next_page_token")
    val nextPageToken: String
)

@JsonClass(generateAdapter = true)
data class Event(
    @Json(name = "end")
    val end: String,
    @Json(name = "start")
    val start: String,
    @Json(name = "title")
    val title: String
): Serializable