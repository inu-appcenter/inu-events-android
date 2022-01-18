package org.inu.events.di

import org.inu.events.data.service.DummyEventService
import org.inu.events.data.service.EventService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppConfigs {
    val eventService: EventService = DummyEventService()
}

fun getService() : EventService {
    return Retrofit.Builder()
        .baseUrl("baseUrl")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(EventService::class.java)
}