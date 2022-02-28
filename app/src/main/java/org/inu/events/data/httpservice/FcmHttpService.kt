package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.AddFcmParams
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmHttpService {

    @POST("/fcm/token")
    fun postFcm(@Body param: AddFcmParams) : Call<Unit>
}