package org.inu.events.data.repository.impl

import org.inu.events.data.httpservice.LikeHttpService
import org.inu.events.data.model.dto.LikeParam
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.LikeRepository

class LikeRepositoryImpl(
        private val httpService: LikeHttpService
) : LikeRepository{
    override fun postLike(param: LikeParam) {
        httpService.postLike(param).execute()
    }

    override fun deleteLike(param:LikeParam) {
        httpService.deleteLike(param).execute()
    }

    override fun getLikes(): List<Event> {
        return httpService.getLikes().execute().body()!!
    }
}