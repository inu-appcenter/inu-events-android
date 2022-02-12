package org.inu.events.data.repository

import org.inu.events.data.model.dto.AddFcmEventParams
import org.inu.events.data.model.dto.UpdateFcmEventParams


interface FcmRepository {
    fun postFcmToken(fcmToken: String)
    fun postFcmEvent(params: AddFcmEventParams)
    fun editFcmEvent(id: Int, params:UpdateFcmEventParams)
}