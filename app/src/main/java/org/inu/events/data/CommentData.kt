package org.inu.events.data

data class CommentData (
    val id: Int,
    val userId: Int?,
    val eventId: Int?,
    val nickName: String,
    val comment: String,
    val profile: String
)
