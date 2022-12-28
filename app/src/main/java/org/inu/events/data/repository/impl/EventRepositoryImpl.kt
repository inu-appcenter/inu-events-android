package org.inu.events.data.repository.impl

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
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

    override fun getEvents(categoryId: Int, eventStatus: Boolean) = Pager(
        config = PagingConfig(10),
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
        Log.d("tag","이미지 업로드 함수 호출")
        return httpService.uploadImage(image).execute().body()!!
    }

    override fun createEventPageSource(categoryId: Int, eventStatus: Boolean): EventPagingSource {
        return EventPagingSource(httpService, categoryId, eventStatus)
    }
}