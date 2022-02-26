package org.inu.events.data.repository

import org.inu.events.data.model.dto.NotificationParams
import org.inu.events.data.model.dto.UpdateNotificationParams

interface NotificationRepository {
    fun getNotifications(): List<UpdateNotificationParams>
    fun postNotification(params: NotificationParams)
    fun deleteNotification(params: NotificationParams)
}