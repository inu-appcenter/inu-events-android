package org.inu.events.data.repository

import org.inu.events.data.model.dto.LikeParam
import org.inu.events.data.model.entity.Event


interface LikeRepository {
    fun postLike(param:LikeParam)
    fun deleteLike(param:LikeParam)
    fun getLikes(): List<Event>
}