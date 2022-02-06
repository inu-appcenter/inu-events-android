package org.inu.events.common.extension

import org.inu.events.common.exception.RetrofitException
import retrofit2.Call

fun <T> Call<T>.orThrow(): T? {
    val result = execute()

    if (result.isSuccessful) {
        return result.body()
    } else {
        throw RetrofitException(result)
    }
}