package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.AddBlockParams
import org.inu.events.data.model.entity.Block
import retrofit2.Call
import retrofit2.http.*

interface BlockHttpService {
    // todo - 서버 이후 수정
    @POST("/blocks")
    fun addBlockUser(
        @Body params: AddBlockParams
    ): Call<Unit>

    @GET("/blocks")
    fun fetchBlockUser(
    ): Call<List<Block>>

    @DELETE("/blocks")
    fun deleteBlockUser(
        @Path("blockUserId") targetUserId: Int
    ): Call<Unit>
}