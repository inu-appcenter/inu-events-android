package org.inu.events.data.httpservice

import retrofit2.Call
import retrofit2.http.POST

interface FcmHttpService {

    @POST("/fcm/token")
    fun postFcm() : Call<Unit>
}