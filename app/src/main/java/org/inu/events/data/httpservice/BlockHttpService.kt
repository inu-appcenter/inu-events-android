package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.AddBlockParams
import org.inu.events.data.model.entity.Block
import org.inu.events.data.model.entity.BlockedUser
import retrofit2.Call
import retrofit2.http.*

interface BlockHttpService {
    // todo - 서버 이후 수정
    @POST("/blocks")
    fun postBlockUser(
        @Body params: AddBlockParams
    ): Call<Unit>

    @GET("/blocks")
    fun fetchBlockUser(
    ): Call<List<BlockedUser>>

    @HTTP(method = "DELETE", path = "/blocks", hasBody = true)
    fun deleteBlockUser(
        @Body targetUserId: AddBlockParams
    ): Call<Unit>
}