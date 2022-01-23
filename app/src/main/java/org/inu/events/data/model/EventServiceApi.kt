package org.inu.events.data.model

import okhttp3.MultipartBody
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UploadImageResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface EventServiceApi {
    @POST("/events")
    fun addEvents(
        @Body params: AddEventParams
    ): Call<Unit>

    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<UploadImageResult>
}