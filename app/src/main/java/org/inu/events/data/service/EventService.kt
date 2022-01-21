package org.inu.events.data.service

import org.inu.events.data.model.Article
import retrofit2.http.GET
import retrofit2.http.Path

interface EventService {
    @GET("/events/")
    fun fetchEvent() : List<Article>

    @GET("/events/{eventId}")
    fun getEventDetail(@Path("eventId") eventId: Int?) : Article
}