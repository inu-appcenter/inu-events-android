package org.inu.events.data.repository.impl

import android.util.Log
import org.inu.events.data.httpservice.UserHttpService
import org.inu.events.data.model.entity.User
import org.inu.events.data.repository.UserRepository
import java.lang.Exception

class UserRepositoryImpl(
    private val httpService: UserHttpService
) : UserRepository {

    override fun getUser(userId: Int): User {
        return httpService.getUser(userId).execute().body()!!
    }

    override suspend fun getMe(): User {
        val response = httpService.getMe()

        try {
            return response.body()!!
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        throw Exception("")
    }
}