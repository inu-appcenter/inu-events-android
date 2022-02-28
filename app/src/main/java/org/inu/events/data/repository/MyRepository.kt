package org.inu.events.data.repository

import org.inu.events.data.model.entity.Event

interface MyRepository {
    fun getComments(): List<Event>
    fun getEvents(): List<Event>
}