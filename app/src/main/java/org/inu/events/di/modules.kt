package org.inu.events.di

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import org.inu.events.common.db.SharedPreferenceWrapper
import org.inu.events.data.httpservice.*
import org.inu.events.data.repository.*
import org.inu.events.data.repository.impl.AccountRepositoryImpl
import org.inu.events.data.repository.impl.CommentRepositoryImpl
import org.inu.events.data.repository.impl.EventRepositoryImpl
import org.inu.events.data.repository.mock.FcmRepositoryMock
import org.inu.events.data.repository.mock.UserRepositoryMock
import org.inu.events.service.LoginService
import org.inu.events.service.UserService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager

val cookieJar = JavaNetCookieJar(CookieManager())
val okHttpClient = OkHttpClient.Builder()
    .cookieJar(cookieJar)
    .build()

inline fun <reified T> buildRetrofitService(): T {
    return Retrofit.Builder()
        .baseUrl("http://uniletter.inuappcenter.kr/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
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

    single<FcmHttpService>{
        buildRetrofitService()
    }

    single<EventRepository> {
        //EventRepositoryMock()
        EventRepositoryImpl(
            httpService = get()
        )
    }

    single<CommentRepository> {
        //TODO 지금은 임시 데이터
        //CommentRepositoryMock()
        CommentRepositoryImpl(httpService = get())
    }

    single<UserRepository> {
        //TODO 지금은 임시 데이터
        UserRepositoryMock()
    }

    single<FcmRepository>{
        //TODO 지금은 임시 데이터
        FcmRepositoryMock()
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

    single<UserService> {
        UserService(userRepository = get())
    }
}