package org.inu.events.data.repository.mock

import org.inu.events.data.model.dto.UpdateUserParams
import org.inu.events.data.model.entity.User
import org.inu.events.data.repository.UserRepository

class UserRepositoryMock : UserRepository {
    private val u = listOf(
        User(0, "123@gmail.com", "수달", "??", "0", "2022-01-21"),
        User(1, "456@gmail.com", "닭발", "??", "1", "2022-01-22"),
        User(null, "789@gmail.com", "망고", "??", "2", "2022-01-20")
    )

    override fun getUser(userId: Int): User {
        return u.find { it.id == userId }!!
    }

    override suspend fun getMe(): User {
        return u[0]
    }

    override suspend fun updateUser(user: UpdateUserParams) {
        TODO("Not yet implemented")
    }
}