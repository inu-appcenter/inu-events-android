package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.BlockHttpService
import org.inu.events.data.model.dto.AddBlockParams
import org.inu.events.data.model.entity.Block
import org.inu.events.data.repository.BlockRepository

class BlockRepositoryImpl(private val httpService: BlockHttpService) : BlockRepository {
    override fun postBlockUsers(params: AddBlockParams) {
        httpService.addBlockUser(params).execute()
    }

    override fun getBlockUsers(): List<Block> {
        return httpService.fetchBlockUser().execute().body()!!
    }

    override fun deleteBlockUsers(targetUserId: Int) {
        httpService.deleteBlockUser(targetUserId).execute()
    }
}