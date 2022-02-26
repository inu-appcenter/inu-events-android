package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.NotificationParams
import org.inu.events.data.model.dto.UpdateNotificationParams
import retrofit2.Call
import retrofit2.http.*

interface NotificationHttpService {
    @POST("/notifications")
    fun postNotification(
        @Body params: NotificationParams
    ): Call<Unit>

    @HTTP(method ="DELETE", path = "/notifications", hasBody = true)
    fun deleteNotification(
        @Body params: NotificationParams
    ):Call<Unit>

    @GET("/notifications")
    fun getNotifications():Call<List<UpdateNotificationParams>>

}