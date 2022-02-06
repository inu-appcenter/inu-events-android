package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.LoginParams
import org.inu.events.data.model.dto.LoginResult
import org.inu.events.data.model.dto.RememberedLoginParams
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountHttpService {
    @POST("/login")
    fun login(@Body params: LoginParams): Call<LoginResult>

    @POST("/login")
    fun login(@Body params: RememberedLoginParams): Call<LoginResult>
}