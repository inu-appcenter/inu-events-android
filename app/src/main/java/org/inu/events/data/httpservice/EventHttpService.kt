package org.inu.events.data.httpservice

import okhttp3.MultipartBody
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.entity.Event
import org.inu.events.data.model.dto.UploadImageResult
import retrofit2.Call
import retrofit2.http.*

interface EventHttpService {
    @GET("/events")
    fun fetchEvent(): Call<List<Event>>

    @POST("/events")
    fun postEvent(@Body params: AddEventParams): Call<Unit>

    @GET("/events/{eventId}")
    fun getEventDetail(@Path("eventId") eventId: Int?): Call<Event>

    @DELETE("/events/{eventId}")
    fun deleteEvent(@Path("eventId") eventId: Int?): Call<Unit>

    @POST("/images")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<UploadImageResult>
}