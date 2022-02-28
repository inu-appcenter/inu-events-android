package org.inu.events.di

import com.google.gson.GsonBuilder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.inu.events.common.db.SharedPreferenceWrapper
import org.inu.events.data.httpservice.*
import org.inu.events.data.repository.*
import org.inu.events.data.repository.impl.*
import org.inu.events.data.repository.mock.NotificationRepositoryMock
import org.inu.events.data.repository.impl.AccountRepositoryImpl
import org.inu.events.data.repository.impl.CommentRepositoryImpl
import org.inu.events.data.repository.impl.EventRepositoryImpl
import org.inu.events.data.repository.impl.UserRepositoryImpl
import org.inu.events.data.repository.mock.UserRepositoryMock
import org.inu.events.service.LoginService
import org.inu.events.service.UserService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.concurrent.Flow

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
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return interceptor
        }

        fun clearCookie() {
            cookieManager.cookieStore.removeAll()
        }
    }
}