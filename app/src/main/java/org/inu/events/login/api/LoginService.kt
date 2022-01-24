package org.inu.events.login.api

import org.inu.events.login.dao.LoginGoogleRequest
import org.inu.events.login.dao.LoginGoogleResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {
    @POST("oauth2/v4/token")
    fun getAccessToken(
        @Body request: LoginGoogleRequest
    ):
            Call<LoginGoogleResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun sendAccessToken(
        @Body accessToken:String
    ):Call<String>
}