package org.inu.events.data.model.entity

data class Event(
    val id: Int,
    val userId: Int,
    val nickname: String,
    val profileImage: String,
    val title: String,
    val host: String?,
    val category: String,
    val target: String?,
    val startAt: String,
    val endAt: String,
    val contact: String?,
    val location: String?,
    val body: String,
    val imageUuid: String,
    val createdAt: String,
    val wroteByMe: Boolean?,
    var likedByMe: Boolean?,
    val notificationSetByMe: Boolean?,
    val notificationSetFor: String,
    val comments: Int,
    val views: Int,
    val likes: Int,
    val notifications: Int,
    val submissionUrl: String,
) {
    fun isSameListContent(event: Event): Boolean {
        return imageUuid == event.imageUuid ||
                title == event.title ||
                startAt == event.startAt ||
                endAt == event.endAt ||
                category == event.category
    }
}