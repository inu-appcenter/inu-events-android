package org.inu.events.data.repository.mock

import org.inu.events.data.model.entity.Fcm
import org.inu.events.data.repository.FcmRepository

class FcmRepositoryMock: FcmRepository {
    private lateinit var t: Fcm

    override fun postFcmToken(fcmToken: String) {
        t = Fcm(fcmToken = fcmToken)
    }
}