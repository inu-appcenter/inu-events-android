package org.inu.events.data.repository

import okhttp3.MultipartBody
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UpdateEventParams
import org.inu.events.data.model.dto.UploadImageResult
import org.inu.events.data.model.entity.Event

interface EventRepository {
    fun getEvents(): List<Event>
    fun getEvent(eventId: Int): Event
    fun postEvent(params: AddEventParams)
    fun updateEvent(id: Int, params: UpdateEventParams)
    fun deleteEvent(eventId: Int)
    fun uploadImage(image: MultipartBody.Part): UploadImageResult
}