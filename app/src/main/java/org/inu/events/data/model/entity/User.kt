package org.inu.events.data.model.entity

data class User(
    val id:Int?,
    val email:String,
    val nickname:String,
    val oauthProvider:String,
    val oauthId:String,
    val createdAt:String,
    val imageUuid: String? = null
)
