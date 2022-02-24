package org.inu.events.data.httpservice

import org.inu.events.data.model.entity.Event
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface LikeHttpService {
    @POST("/likes")
    fun postLike(@Body eventId : Int) : Call<Unit>

    @DELETE("/likes")
    fun deleteLike(@Body eventId: Int) : Call<Unit>

    @GET("/likes")
    fun getLikes(): Call<List<Event>>
}