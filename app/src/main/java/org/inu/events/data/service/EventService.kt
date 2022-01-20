package org.inu.events.data.service

import org.inu.events.data.model.Article
import retrofit2.http.GET

interface EventService {
    fun fetchEvent() : List<Article>
}