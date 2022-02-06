package org.inu.events.data.model.entity

data class Event(
    var id: Int,
    var host: String,
    var category: String,
    var title: String,
    var body: String,
    var imageUuid: String, //todo - 서버 나오면 string 형으로 바꾸기
    var startAt: String,
    var endAt: String,
    var createdAt: String,
    var userId: Int,
)