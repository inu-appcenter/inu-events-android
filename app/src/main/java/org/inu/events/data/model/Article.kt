package org.inu.events.data.model

data class Article(
    var id: Int,
    var host: String,
    var category: String,
    var title: String,
    var body: String,
    var image_uuid: Int, //todo - 서버 나오면 string 형으로 바꾸기
    var start_at: String,
    var end_at: String,
    var created_at: String,
    var user_id: Int,
)