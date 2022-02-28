package org.inu.events.data.httpservice

import org.inu.events.data.model.entity.Event
import retrofit2.Call
import retrofit2.http.GET

interface MyHttpService {
    @GET("/events-ive-commented")
    fun getComments(): Call<List<Event>>

    @GET("/myevents")
    fun getEvents(): Call<List<Event>>
}