package org.inu.events.common.exception

import retrofit2.Response

class RetrofitException(private val response: Response<*>) :
    RuntimeException(response.message()) {
}