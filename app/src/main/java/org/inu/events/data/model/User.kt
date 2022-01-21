package org.inu.events.data.model

data class User(
    val id:Int,
    val email:String,
    val nickname:String,
    val oauth_provider:String,
    val oauth_id:String,
    val created_at:String
)
