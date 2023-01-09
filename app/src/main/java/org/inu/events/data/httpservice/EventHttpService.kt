package org.inu.events.data.httpservice

import androidx.paging.PagingSource
import okhttp3.MultipartBody
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UpdateEventParams
import org.inu.events.data.model.dto.UploadImageResult
import org.inu.events.data.model.entity.Event
import retrofit2.Call
import retrofit2.http.*

interface EventHttpService {

    @Headers("Connection: close")
    @GET("/events-by-category")
    fun fetchEvents(
        @Query("categoryId") categoryId: Int = 0,
        @Query("eventStatus") eventStatus: Boolean = false,
        @Query("pageNum") pageNum: Int = 0,
        @Query("pageSize") pageSize: Int = 20
    ): Call<List<Event>>

    @POST("/events")
    fun postEvent(@Body params: AddEventParams): Call<Unit>

    @PATCH("/events/{eventId}")
    fun editEvent(
        @Path("eventId") eventId: Int,
        @Body params: UpdateEventParams
    ): Call<Unit>

    @GET("/events/{eventId}")
    fun getEventDetail(@Path("eventId") eventId: Int?): Call<Event>

    @DELETE("/events/{eventId}")
    fun deleteEvent(@Path("eventId") eventId: Int): Call<Unit>

    @Multipart
    @POST("/images")
    fun uploadImage(
        @Part file: MultipartBody.Part
    ): Call<UploadImageResult>

    @GET("/events-by-search-with-filtering")
    suspend fun searchEvents(
        @Query("categoryId") categoryId: Int,
        @Query("eventStatus") eventStatus: Boolean,
        @Query("content") content: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
    ): List<Event>
}