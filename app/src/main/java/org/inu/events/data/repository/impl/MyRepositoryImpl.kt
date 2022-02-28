package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.MyHttpService
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.MyRepository

class MyRepositoryImpl(val httpService: MyHttpService) : MyRepository {
    override fun getComments(): List<Event> = httpService.getComments().execute().body()!!
    override fun getEvents(): List<Event> = httpService.getEvents().execute().body()!!
}