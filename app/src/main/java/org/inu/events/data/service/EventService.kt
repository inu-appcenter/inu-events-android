package org.inu.events.data.service

import org.inu.events.data.model.Article
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventService {
    @GET("/events/")
    fun fetchEvent() : List<Article>

    @POST("/events/")
    fun postEvent(event: Article)

    @GET("/events/{eventId}")
    fun getEventDetail(@Path("eventId") eventId: Int?) : Article

    @DELETE("/events/{eventId}")
    fun deleteEvent(@Path("eventId") eventId: Int?)
}