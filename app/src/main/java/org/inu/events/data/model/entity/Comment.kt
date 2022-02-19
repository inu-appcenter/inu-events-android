package org.inu.events.data.model.entity

data class Comment (
    val id: Int,
    val userId: Int?,
    val eventId: Int?,
    val nickname: String,
    val content: String,
    val profileImage: String,
    val wroteByMe: Boolean?
)
