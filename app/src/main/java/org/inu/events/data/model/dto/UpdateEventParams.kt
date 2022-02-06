package org.inu.events.data.model.dto

data class UpdateEventParams(
    val id: Int,
    val host: String,
    val category: String,
    val title: String,
    val body: String,
    val imageUuid: String,
    val startAt: String,
    val endAt: String,
)