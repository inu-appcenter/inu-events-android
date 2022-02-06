package org.inu.events.data.model.dto

data class AddCommentParams(
    val eventId: Int,
    val content: String
)