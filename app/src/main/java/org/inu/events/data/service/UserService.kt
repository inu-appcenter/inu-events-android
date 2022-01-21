package org.inu.events.data.service

import org.inu.events.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/users/")
    fun fetchUser() : List<User>

    @GET("/users/{userId}")
    fun getUser(@Path("userId") userId: Int?): User
}