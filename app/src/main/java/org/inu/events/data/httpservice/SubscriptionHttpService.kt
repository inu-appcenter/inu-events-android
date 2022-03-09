package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.Subscribing
import org.inu.events.data.model.dto.Topics
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface SubscriptionHttpService {
    @PUT("/subscription/subscribing")
    fun putSubscribing(
        @Body param: Subscribing
    ): Call<Unit>

    @GET("/subscription/subscribing")
    fun getSubscribing(
    ) : Call<Subscribing>

    @PUT("/subscription/topics")
    fun putTopics(
        @Body param: Topics
    ) : Call<Unit>

    @GET("/subscription/topics")
    fun getTopics(
    ) : Call<Topics>
}