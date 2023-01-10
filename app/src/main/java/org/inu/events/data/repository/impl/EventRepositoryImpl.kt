package org.inu.events.data.repository.impl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import org.inu.events.data.httpservice.EventHttpService
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UpdateEventParams
import org.inu.events.data.model.dto.UploadImageResult
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.inu.events.data.source.EventPagingSource
import org.inu.events.data.source.EventSearchPagingSource

class EventRepositoryImpl(
    private val httpService: EventHttpService
) : EventRepository {

    override fun getEvents(categoryId: Int, eventStatus: Boolean) = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            createEventPageSource(categoryId, eventStatus)
        }
    ).flow

    override fun getEvent(eventId: Int): Event {
        return httpService.getEventDetail(eventId).execute().body()!!
    }

    override fun postEvent(params: AddEventParams) {
        httpService.postEvent(params).execute()
    }

    override fun updateEvent(id: Int, params: UpdateEventParams) {
        httpService.editEvent(id, params).execute()
    }

    override fun deleteEvent(eventId: Int) {
        httpService.deleteEvent(eventId).execute()
    }

    override fun uploadImage(image: MultipartBody.Part): UploadImageResult {
        Log.d("tag", "이미지 업로드 함수 호출")
        return httpService.uploadImage(image).execute().body()!!
    }

    override fun createEventPageSource(categoryId: Int, eventStatus: Boolean): EventPagingSource {
        return EventPagingSource(httpService, categoryId, eventStatus)
    }

    override fun searchEvents(
        categoryId: Int,
        eventStatus: Boolean,
        content: String,
    ): Flow<PagingData<Event>> = Pager(
        config = PagingConfig(
            pageSize = 10,
        ),
        pagingSourceFactory = {
            EventSearchPagingSource(
                httpService = httpService,
                categoryId = categoryId,
                eventStatus = eventStatus,
                content = content,
            )
        }
    ).flow
}