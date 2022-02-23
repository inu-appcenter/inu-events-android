package org.inu.events.data.repository.mock

import org.inu.events.data.model.dto.NotificationParams
import org.inu.events.data.model.dto.UpdateNotificationParams
import org.inu.events.data.repository.NotificationRepository

class NotificationRepositoryMock: NotificationRepository {
    override fun getNotifications(): List<UpdateNotificationParams> {
        TODO("Not yet implemented")
    }

    override fun postNotification(params: NotificationParams) {
        TODO("Not yet implemented")
    }

    override fun deleteNotification(params: NotificationParams) {
        TODO("Not yet implemented")
    }
}