package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.FcmHttpService
import org.inu.events.data.model.dto.AddFcmParams
import org.inu.events.data.repository.FcmRepository

class FcmRepositoryImpl(
    private val httpService: FcmHttpService
): FcmRepository{
    override fun postFcm(token: AddFcmParams) {
        httpService.postFcm(token).execute()
    }
}