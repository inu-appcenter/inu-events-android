package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.OAuthLoginParams
import org.inu.events.data.model.dto.LoginResult
import org.inu.events.data.model.dto.RememberedLoginParams
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountHttpService {
    @POST("/login/oauth/google")
    fun login(@Body params: OAuthLoginParams): Call<LoginResult>

    @POST("/login/remembered")
    fun login(@Body params: RememberedLoginParams): Call<LoginResult>
}