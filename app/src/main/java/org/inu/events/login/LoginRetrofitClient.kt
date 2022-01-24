package org.inu.events.login

import org.inu.events.login.api.LoginService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginRetrofitClient {
    fun retrofit(baseUrl:String): LoginService {
        val instance : LoginService by lazy{
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(LoginService::class.java)
        }
        return instance
    }
}