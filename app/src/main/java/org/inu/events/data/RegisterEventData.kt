package org.inu.events.data


data class RegisterEventData(
    val title: String = "",
    val organization: String = "",
    val classification: String = "",
    val period: String = "",
    val target: String = "",
    val content: String = "",
    val imgResId: Int= 1,
    val phase: Int = 1,
    val selectedItemPosition: Int = 0
)
