package org.inu.events.di

import org.inu.events.data.httpservice.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppConfigs {
}

fun getService() : EventHttpService {
    return Retrofit.Builder()
        .baseUrl("http://uniletter.inuappcenter.kr/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(EventHttpService::class.java)
}

