package org.inu.events.data.httpservice

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmHttpService {
    @POST("/fcmToken")
    fun postFcmToken(
        @Body params: String
    ): Call<Unit>
}