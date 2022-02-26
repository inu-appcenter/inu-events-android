package org.inu.events.common.threading

import kotlinx.coroutines.*

fun <T> execute(body: () -> T): Launcher<T> {
    val job = CoroutineScope(Dispatchers.IO).async {
        body()
    }

    return Launcher(job)
}
