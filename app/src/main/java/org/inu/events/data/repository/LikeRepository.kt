package org.inu.events.data.repository

import org.inu.events.data.model.entity.Event


interface LikeRepository {
    fun postLike(eventId : Int)
    fun deleteLike(eventId: Int)
    fun getLikes(): List<Event>
}