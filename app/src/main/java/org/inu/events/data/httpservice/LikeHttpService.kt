package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.LikeParam
import org.inu.events.data.model.entity.Event
import retrofit2.Call
import retrofit2.http.*

interface LikeHttpService {
    @POST("/likes")
    fun postLike(@Body param: LikeParam) : Call<Unit>

    @HTTP(method ="DELETE", path = "/likes", hasBody = true)
    fun deleteLike(
        @Body param:LikeParam
    ) : Call<Unit>

    @GET("/likes")
    fun getLikes(): Call<List<Event>>
}