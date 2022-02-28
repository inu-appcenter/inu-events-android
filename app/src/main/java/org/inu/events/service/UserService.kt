package org.inu.events.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.inu.events.data.model.dto.UpdateUserParams
import org.inu.events.data.model.dto.UpdateUserParamsOnlyNickname
import org.inu.events.data.model.entity.User
import org.inu.events.data.repository.UserRepository

class UserService(private val userRepository: UserRepository) {
    suspend fun getUserInfo() : User = withContext(Dispatchers.IO) {
        userRepository.getMe()
    }

    suspend fun updateUser(user: UpdateUserParams) {
        withContext(Dispatchers.IO) {
            if(user.imageUuid == null) {
                val params = UpdateUserParamsOnlyNickname(user.nickname)
                userRepository.updateUser(params)
            } else {
                userRepository.updateUser(user)
            }
        }
    }
}