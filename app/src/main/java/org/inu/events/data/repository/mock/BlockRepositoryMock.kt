package org.inu.events.data.repository.mock

import org.inu.events.data.model.dto.AddBlockParams
import org.inu.events.data.model.entity.Block
import org.inu.events.data.model.entity.Comment
import org.inu.events.data.repository.BlockRepository

class BlockRepositoryMock : BlockRepository {
    private val c = mutableListOf(
        Block(
            1,
            3,
            "2022-03-06 07:00:01"
        )
    )

    override fun postBlockUsers(params: AddBlockParams) {
        c.add(
            Block(
                userId = 4,
                blockUserId = 1,
                blockDate = "2022-03-06 07:00:03"
            )
        )
    }
}