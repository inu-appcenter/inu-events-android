package org.inu.events.data.repository

import org.inu.events.data.model.entity.Fcm


interface FcmRepository {
    fun postFcmToken(fcmToken: String)
}