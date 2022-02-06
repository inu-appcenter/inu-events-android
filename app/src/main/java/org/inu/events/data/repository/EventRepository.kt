package org.inu.events.data.repository

import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UpdateEventParams
import org.inu.events.data.model.entity.Event

interface EventRepository {
    fun getEvents(): List<Event>
    fun getEvent(eventId: Int): Event
    fun postEvent(params: AddEventParams)
    fun updateEvent(params: UpdateEventParams)
    fun deleteEvent(eventId: Int)
}