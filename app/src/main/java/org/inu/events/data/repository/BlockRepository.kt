package org.inu.events.data.repository

import org.inu.events.data.model.dto.AddBlockParams
import org.inu.events.data.model.entity.BlockedUser

interface BlockRepository {
    fun postBlockUser(params: AddBlockParams)
    fun getBlockUsers() : List<BlockedUser>
    fun deleteBlockUsers(targetUserId: AddBlockParams)
}