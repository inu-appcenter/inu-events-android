package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.BlockHttpService
import org.inu.events.data.model.dto.AddBlockParams
import org.inu.events.data.model.entity.Block
import org.inu.events.data.model.entity.BlockedUser
import org.inu.events.data.repository.BlockRepository

class BlockRepositoryImpl(private val httpService: BlockHttpService) : BlockRepository {
    override fun postBlockUser(params: AddBlockParams) {
        httpService.postBlockUser(params).execute()
    }

    override fun getBlockUsers(): List<BlockedUser> {
        return httpService.fetchBlockUser().execute().body()!!
    }

    override fun deleteBlockUsers(targetUserId: AddBlockParams) {
        httpService.deleteBlockUser(targetUserId).execute()
    }
}