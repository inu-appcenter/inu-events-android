package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.SubscribeHttpService
import org.inu.events.data.model.dto.AddTopics
import org.inu.events.data.repository.SubscribeRepository

class SubscribeRepositoryImpl(
    private val httpService: SubscribeHttpService
) : SubscribeRepository{
    override fun putSubscribing(subscribing: Boolean) {
        httpService.putSubscribing(subscribing).execute()
    }

    override fun getSubscribing(): Boolean {
        return httpService.getSubscribing().execute().body()!!
    }

    override fun putTopics(param: AddTopics) {
        httpService.putTopics(param).execute()
    }

    override fun getTopics(): ArrayList<String> {
        return httpService.getTopics().execute().body()!!
    }
}