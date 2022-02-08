package org.inu.events.data.model.entity

data class Comment (
    val id: Int,
    val userId: Int?,
    val eventId: Int?,
    val nickName: String,
    val content: String,
    val profile: String,
    val wroteByMe: Boolean?
)
