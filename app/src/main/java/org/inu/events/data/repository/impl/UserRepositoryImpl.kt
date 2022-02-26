package org.inu.events.data.repository.impl

import android.util.Log
import org.inu.events.data.httpservice.UserHttpService
import org.inu.events.data.model.dto.UpdateUserParams
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
        try {
            val response = httpService.getMe().execute()
            if(response.isSuccessful) {
                return response.body()!!
            } else {
                Log.e("네트워크 요청", "실패했다...")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        throw Exception("error code 1")
    }

    override suspend fun updateUser(user: UpdateUserParams) {
        try {
            httpService.updateUser(user).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}