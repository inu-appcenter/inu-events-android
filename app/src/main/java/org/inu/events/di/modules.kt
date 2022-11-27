package org.inu.events.di

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import org.inu.events.common.db.SharedPreferenceWrapper
import org.inu.events.data.httpservice.*
import org.inu.events.data.repository.*
import org.inu.events.data.repository.impl.*
import org.inu.events.service.LoginService
import org.inu.events.service.UserService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager


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

    single<BlockHttpService> {
        buildRetrofitService()
    }

    single<AccountHttpService> {
        buildRetrofitService()
    }

    single<NotificationHttpService>{
        buildRetrofitService()
    }

    single<LikeHttpService>{
        buildRetrofitService()
    }

    single<SubscriptionHttpService>{
        buildRetrofitService()
    }

    single<FcmHttpService>{
        buildRetrofitService()
    }


    single<EventRepository> {
//        EventRepositoryMock()
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
        UserRepositoryImpl(get())
//        UserRepositoryMock()
    }

    single<BlockRepository> {
        // TODO -지금은 임시 데이터
    //    BlockRepositoryMock()
      BlockRepositoryImpl(get())
    }

    single<NotificationRepository>{
        NotificationRepositoryImpl(httpService = get())
    }

    single<LikeRepository>{
        LikeRepositoryImpl(httpService = get())
    }

    single<SubscriptionRepository>{
        SubscriptionRepositoryImpl(httpService = get())
    }

    single<FcmRepository>{
        FcmRepositoryImpl(httpService = get())
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

    single<MyHttpService> { buildRetrofitService() }
    single<MyRepository> { MyRepositoryImpl( get() ) }
}

inline fun <reified T> buildRetrofitService(): T {
    return Retrofit.Builder()
        .baseUrl("http://uniletter.inuappcenter.kr/")
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder().serializeNulls().create()
        ))
        .client(OkHttpClientFactory.create())
        .build()
        .create(T::class.java)
}

class OkHttpClientFactory {
    companion object {
        private val cookieManager = CookieManager()
        private val cookieJar = JavaNetCookieJar(cookieManager)

        fun create() : OkHttpClient {
            return OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(createLoggingInterceptor())
                .build()
        }


        private fun createLoggingInterceptor() : HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor { message ->
                if (!message.contains("�")) {
                    Platform.get().log(message)
                }
            }

            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return interceptor
        }

        fun clearCookie() {
            cookieManager.cookieStore.removeAll()
        }
    }
}