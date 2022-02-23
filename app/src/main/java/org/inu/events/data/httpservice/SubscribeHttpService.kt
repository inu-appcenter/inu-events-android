package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.AddTopics
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface SubscribeHttpService {
    @PUT("/subscribe/subscribing")
    fun putSubscribing(
        @Body subscribing: Boolean
    ): Call<Unit>

    @GET("/subscribe/subscribing")
    fun getSubscribing(
    ) : Call<Boolean>

    @PUT("subscribe/topics")
    fun putTopics(
        @Body param: AddTopics
    ) : Call<Unit>

    @GET("subscribe/topics")
    fun getTopics(
    ) : Call<ArrayList<String>>
}