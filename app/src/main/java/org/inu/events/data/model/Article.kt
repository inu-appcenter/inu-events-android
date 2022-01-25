package org.inu.events.data.model

data class Article(
    val id: Int,
    val host: String,
    val category: String,
    val title: String,
    val body: String,
    val image_uuid: Int, //todo - 서버 나오면 string 형으로 바꾸기
    val start_at: String,
    val end_at: String,
    val created_at: String,
    val user_id: Int,
)