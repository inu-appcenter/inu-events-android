package org.inu.events.data.model.dto

import org.inu.events.data.model.entity.Event

data class UpdateNotificationParams(
    val event:Event,
    val setFor:String
)