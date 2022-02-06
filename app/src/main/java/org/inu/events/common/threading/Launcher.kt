package org.inu.events.common.threading

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class Launcher<T>(private val job: Deferred<T>) {

    private var onSuccess: ((T) -> Unit)? = null
    private var onFailure: ((Throwable) -> Unit)? = null

    fun then(body: (T) -> Unit): Launcher<T> {
        onSuccess = body
        return this.also { runIfAllConditionsMet() }
    }

    fun catch(body: (Throwable) -> Unit): Launcher<T> {
        onFailure = body
        return this.also { runIfAllConditionsMet() }
    }

    private fun runIfAllConditionsMet() {
        onSuccess ?: return
        onFailure ?: return

        MainScope().launch {
            try {
                onSuccess?.let { it(job.await()) }
            } catch (e: Throwable) {
                onFailure?.let { it(e) }
            }
        }
    }
}