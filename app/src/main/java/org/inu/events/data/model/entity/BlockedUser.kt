package org.inu.events.data.model.entity

data class BlockedUser (
    val user: User,
    val blockedAt: String
)