package org.inu.events.data.service

import org.inu.events.data.HomeData
import retrofit2.http.GET

interface EventService {
    fun fetchEvent() : ArrayList<HomeData>
}