package org.inu.events.data.httpservice

import org.inu.events.data.model.dto.UpdateUserParams
import org.inu.events.data.model.dto.UpdateUserParamsOnlyNickname
import org.inu.events.data.model.entity.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserHttpService {
    @GET("/users/{userId}")
    fun getUser(@Path("userId") userId: Int?): Call<User>

    @GET("/me")
    fun getMe(): Call<User>

    @PATCH("/me")
    fun updateUser(@Body user: UpdateUserParams): Call<Unit>

    @PATCH("/me")
    fun updateUser(@Body user: UpdateUserParamsOnlyNickname): Call<Unit>
}