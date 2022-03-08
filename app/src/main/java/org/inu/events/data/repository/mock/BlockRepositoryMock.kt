package org.inu.events.data.repository.mock

import org.inu.events.data.model.dto.AddBlockParams
import org.inu.events.data.model.entity.Block
import org.inu.events.data.model.entity.Comment
import org.inu.events.data.repository.BlockRepository

class BlockRepositoryMock : BlockRepository {
    private val c = mutableListOf(
        Block(
            id = 1
        )
    )

    override fun postBlockUsers(params: AddBlockParams) {
        c.add(
            Block(
                id = 4
            )
        )
    }

    override fun getBlockUsers(): List<Block> {
        return c
    }

    override fun deleteBlockUsers(targetUserId: Int) {
        c.removeIf { it.id == targetUserId }
    }
}