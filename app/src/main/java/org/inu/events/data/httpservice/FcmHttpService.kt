package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.AddCommentParams
import org.inu.events.data.model.dto.AddFcmEventParams
import org.inu.events.data.model.dto.UpdateFcmEventParams
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface FcmHttpService {
    @POST("/fcmToken")
    fun postFcmToken(
        @Body params: String
    ): Call<Unit>

    @POST("/fcmEvent")
    fun postFcmEvent(
        @Body params: AddFcmEventParams
    ):Call<Unit>

    @PATCH("/fcmEvent/{eventId}")
    fun editFcmEvent(
        @Path("eventId") eventId:Int,
        @Body isFcm: UpdateFcmEventParams
    ):Call<Unit>

}