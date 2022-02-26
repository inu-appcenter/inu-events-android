package org.inu.events.data.model.dto

data class RememberedLoginParams(
    /**
     * 사용자 ID(PK).
     */
    val id: Int,

    /**
     * 자동로그인 토큰.
     */
    val token: String
)
