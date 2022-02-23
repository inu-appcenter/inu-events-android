package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.NotificationHttpService
import org.inu.events.data.model.dto.NotificationParams
import org.inu.events.data.model.dto.UpdateNotificationParams
import org.inu.events.data.repository.NotificationRepository
import retrofit2.http.Body

class NotificationRepositoryImpl(
    private val httpService: NotificationHttpService
) : NotificationRepository {


    override fun getNotifications(): List<UpdateNotificationParams> {
        return httpService.getNotifications().execute().body()!!
    }

    override fun postNotification(params: NotificationParams) {
        httpService.postNotification(params).execute()
    }

    override fun deleteNotification(params: NotificationParams) {
        httpService.deleteNotification(params).execute()
    }
}