package org.inu.events.data.httpservice

import org.inu.events.data.model.entity.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserHttpService {
    @GET("/users/{userId}")
    fun getUser(@Path("userId") userId: Int?): Call<User>

    @GET("/me")
    fun getMe(): Call<User>
}