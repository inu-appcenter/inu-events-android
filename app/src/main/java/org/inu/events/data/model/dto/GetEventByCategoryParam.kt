package org.inu.events.data.model.dto

data class GetEventByCategoryParam(
    var categoryId: Int,
    val eventStatus: Boolean,
    val pageNum: Int?,
    val pageSize: Int?
)
