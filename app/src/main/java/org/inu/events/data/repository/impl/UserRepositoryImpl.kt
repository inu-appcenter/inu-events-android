package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.UserHttpService
import org.inu.events.data.model.entity.User
import org.inu.events.data.repository.UserRepository

class UserRepositoryImpl(
    private val httpService: UserHttpService
) : UserRepository {

    override fun getUser(userId: Int): User {
        return httpService.getUser(userId).execute().body()!!
    }

    override fun getMe(): User {
        return httpService.getMe().execute().body()!!
    }
}