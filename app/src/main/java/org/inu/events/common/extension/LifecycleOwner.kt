package org.inu.events.common.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, observer: Observer<in T>) {
    liveData.observe(this, observer)
}

fun <T> LifecycleOwner.observeNonNull(liveData: LiveData<T>, observer: Observer<in T>) {
    liveData.observe(this) {
        it ?: return@observe

        observer.onChanged(it)
    }
}