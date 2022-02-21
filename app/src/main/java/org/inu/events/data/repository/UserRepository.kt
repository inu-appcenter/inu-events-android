package org.inu.events.data.repository

import org.inu.events.data.model.entity.Account
import org.inu.events.data.model.entity.User

interface UserRepository {
    fun getUser(userId: Int): User
    suspend fun getMe(): User
}