package org.inu.events.data.repository

import org.inu.events.data.model.dto.AddFcmParams

interface FcmRepository {
    fun postFcm(token:AddFcmParams)
}