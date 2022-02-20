package org.inu.events.data.repository.impl

import okhttp3.MultipartBody
import org.inu.events.data.httpservice.EventHttpService
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UpdateEventParams
import org.inu.events.data.model.dto.UploadImageResult
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository

class EventRepositoryImpl(
    private val httpService: EventHttpService
) : EventRepository {

    override fun getEvents(): List<Event> {
        return httpService.fetchEvent().execute().body()!!
    }

    override fun getEvent(eventId: Int): Event {
        return httpService.getEventDetail(eventId).execute().body()!!
    }

    override fun postEvent(params: AddEventParams) {
        httpService.postEvent(params).execute()
    }

    override fun updateEvent(id: Int, params: UpdateEventParams) {
        httpService.editEvent(id, params)
    }

    override fun deleteEvent(eventId: Int) {
        httpService.deleteEvent(eventId).execute()
    }

    override fun uploadImage(image: MultipartBody.Part): UploadImageResult {
        return httpService.uploadImage(image).execute().body()!!
    }
}