package org.inu.events.data.model

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface EventServiceApi {
    @Multipart
    @POST("/events")
    fun addEvents(
        @Part("id") id: Int,
        @Part("host") host: String,
        @Part("category") category: String,
        @Part("title") title: String,
        @Part("body") body: String,
        @Part image_uuid: MultipartBody.Part,
        @Part ("start_at") start_at: String,
        @Part ("end_at") end_at: String,
        @Part ("created_at") created_at: String,
        @Part ("user_id") user_id: Int
    ): Call<String>
}