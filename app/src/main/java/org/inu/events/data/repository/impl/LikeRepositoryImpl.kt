package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.LikeHttpService
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.LikeRepository

class LikeRepositoryImpl(
        private val httpService: LikeHttpService
) : LikeRepository{
    override fun postLike(eventId: Int) {
        httpService.postLike(eventId).execute()
    }

    override fun deleteLike(eventId: Int) {
        httpService.deleteLike(eventId).execute()
    }

    override fun getLikes(): List<Event> {
        return httpService.getLikes().execute().body()!!
    }
}