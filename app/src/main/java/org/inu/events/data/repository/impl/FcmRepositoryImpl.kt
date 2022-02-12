package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.FcmHttpService
import org.inu.events.data.model.entity.Fcm
import org.inu.events.data.repository.FcmRepository

class FcmRepositoryImpl(
    private val httpService: FcmHttpService
) : FcmRepository {

    override fun postFcmToken(fcmToken: String) {
        httpService.postFcmToken(fcmToken).execute()
    }
}