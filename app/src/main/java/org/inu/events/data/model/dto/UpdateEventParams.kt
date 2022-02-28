package org.inu.events.data.model.dto

data class UpdateEventParams(
    val title: String,
    val host: String?,
    val category: String,
    val target: String?,
    val startAt: String,
    val endAt: String,
    val contact: String?,
    val location: String?,
    val body: String,
    val imageUuid: String?
)
