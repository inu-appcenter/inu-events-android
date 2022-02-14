package org.inu.events.di

import org.inu.events.common.db.SharedPreferenceWrapper
import org.inu.events.data.httpservice.AccountHttpService
import org.inu.events.data.httpservice.CommentHttpService
import org.inu.events.data.httpservice.EventHttpService
import org.inu.events.data.httpservice.UserHttpService
import org.inu.events.data.repository.AccountRepository
import org.inu.events.data.repository.CommentRepository
import org.inu.events.data.repository.EventRepository
import org.inu.events.data.repository.UserRepository
import org.inu.events.data.repository.impl.AccountRepositoryImpl
import org.inu.events.data.repository.impl.CommentRepositoryImpl
import org.inu.events.data.repository.impl.EventRepositoryImpl
import org.inu.events.data.repository.mock.CommentRepositoryMock
import org.inu.events.data.repository.mock.EventRepositoryMock
import org.inu.events.data.repository.mock.UserRepositoryMock
import org.inu.events.service.LoginService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

inline fun <reified T> buildRetrofitService(): T {
    return Retrofit.Builder()
        .baseUrl("http://uniletter.inuappcenter.kr/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(T::class.java)
}

val myModules = module {
    single<EventHttpService> {
        buildRetrofitService()
    }

    single<CommentHttpService> {
        buildRetrofitService()
    }

    single<UserHttpService> {
        buildRetrofitService()
    }

    single<AccountHttpService> {
        buildRetrofitService()
    }

    single<EventRepository> {
        //TODO 지금은 임시 데이터
        EventRepositoryMock()
    }

    single<CommentRepository> {
        //TODO 지금은 임시 데이터
        CommentRepositoryMock()
        //CommentRepositoryImpl(get())
    }

    single<UserRepository> {
        //TODO 지금은 임시 데이터
        UserRepositoryMock()
    }

    single<AccountRepository> {
        AccountRepositoryImpl(
            httpService = get(),
            db = get()
        )
    }

    single<LoginService> {
        LoginService(
            accountRepository = get()
        )
    }

    single<SharedPreferenceWrapper> {
        SharedPreferenceWrapper(context = get())
    }
}