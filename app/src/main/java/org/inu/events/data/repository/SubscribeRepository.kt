package org.inu.events.data.repository

import org.inu.events.data.model.dto.AddTopics

interface SubscribeRepository {
    fun putSubscribing(subscribing: Boolean)
    fun getSubscribing() : Boolean
    fun putTopics(param: AddTopics)
    fun getTopics() : ArrayList<String>
}