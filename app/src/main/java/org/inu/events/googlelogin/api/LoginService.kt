package org.inu.events.googlelogin.api

import com.google.gson.GsonBuilder
import org.inu.events.googlelogin.model.LoginGoogleRequestModel
import org.inu.events.googlelogin.model.LoginGoogleResponseModel
import org.inu.events.googlelogin.model.SendAccessTokenModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @POST("oauth2/v4/token")
    fun getAccessToken(
        @Body request: LoginGoogleRequestModel
    ): Call<LoginGoogleResponseModel>

    @POST("login")
    @Headers("content-type: application/json")
    fun sendAccessToken(
        @Body accessToken: SendAccessTokenModel
    ): Call<String>

    companion object {
        private val gson = GsonBuilder().setLenient().create()

        fun loginRetrofit(baseUrl: String): LoginService {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(LoginService::class.java)
        }
    }
}