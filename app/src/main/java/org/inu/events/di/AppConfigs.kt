package org.inu.events.di

import org.inu.events.data.service.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppConfigs {
    val eventService: EventService = DummyEventService()
    val userService: UserService = DummyUserService()
    val commentService: CommentService = DummyCommentService()
}

fun getService() : EventService {
    return Retrofit.Builder()
        .baseUrl("http://uniletter.inuappcenter.kr/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(EventService::class.java)
}

