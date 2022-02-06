package org.inu.events.data.model.dto

data class RememberedLoginParams(
    val email: String,
    val rememberMeToken: String
)
