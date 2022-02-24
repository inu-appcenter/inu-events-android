package org.inu.events.data.repository

import org.inu.events.data.model.dto.Topics

interface SubscriptionRepository {
    fun putSubscribing(subscribing: Boolean)
    fun getSubscribing() : Boolean
    fun putTopics(param: Topics)
    fun getTopics() : Topics
}