package org.inu.events.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UpdateEventParams
import org.inu.events.data.model.dto.UploadImageResult
import org.inu.events.data.model.entity.Event
import org.inu.events.data.source.EventPagingSource

interface EventRepository {
    fun getEvents(categoryId: Int, eventStatus: Boolean): Flow<PagingData<Event>>
    fun getEvent(eventId: Int): Event
    fun postEvent(params: AddEventParams)
    fun updateEvent(id: Int, params: UpdateEventParams)
    fun deleteEvent(eventId: Int)
    fun uploadImage(image: MultipartBody.Part): UploadImageResult
    fun createEventPageSource(categoryId: Int, eventStatus: Boolean): EventPagingSource

    fun searchEvents(
        categoryId: Int,
        eventStatus: Boolean,
        content: String,
    ): Flow<PagingData<Event>>
}