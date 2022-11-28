package org.inu.events.data.model.dto

data class GetEventByCategoryParam(
    var categoryId: Int,
    var eventStatus: Boolean,
    val pageNum: Int?,
    val pageSize: Int?
)
