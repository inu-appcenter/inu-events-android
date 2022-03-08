package org.inu.events.data.repository

import org.inu.events.data.model.dto.AddBlockParams
import org.inu.events.data.model.entity.Block

interface BlockRepository {
    fun postBlockUser(params: AddBlockParams)
    fun getBlockUsers() : List<Block>
    fun deleteBlockUsers(targetUserId: Int)
}