package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.NotificationParams
import org.inu.events.data.model.dto.UpdateNotificationParams
import retrofit2.Call
import retrofit2.http.*

interface NotificationHttpService {
    @POST("/notification")
    fun postNotification(
        @Body params: NotificationParams
    ): Call<Unit>

    @DELETE("/notification")
    fun deleteNotification(
        @Body params: NotificationParams
    ):Call<Unit>

    @GET("/notifications")
    fun getNotifications():Call<List<UpdateNotificationParams>>

}