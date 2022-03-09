package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.SubscriptionHttpService
import org.inu.events.data.model.dto.Subscribing
import org.inu.events.data.model.dto.Topics
import org.inu.events.data.repository.SubscriptionRepository

class SubscriptionRepositoryImpl(
    private val httpService: SubscriptionHttpService
) : SubscriptionRepository{
    override fun putSubscribing(subscribing: Boolean) {
        httpService.putSubscribing(Subscribing(subscribing)).execute()
    }

    override fun getSubscribing(): Boolean {
        return httpService.getSubscribing().execute().body()!!.subscribing
    }

    override fun putTopics(param: Topics) {
        httpService.putTopics(param).execute()
    }

    override fun getTopics(): Topics {
        return httpService.getTopics().execute().body()!!
    }
}