package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.AddBlockParams
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BlockHttpService {
    // todo - 서버 이후 수정
    @POST("/blocks")
    fun addBlockUser(
        @Body params: AddBlockParams
    ): Call<Unit>
}