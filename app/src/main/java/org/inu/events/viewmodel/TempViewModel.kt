package org.inu.events.viewmodel

import androidx.lifecycle.ViewModel
import org.inu.events.common.util.SingleLiveEvent

class TempViewModel : ViewModel() {
    val onNextEvent = SingleLiveEvent<Any>()

    fun onClickNext() {
        onNextEvent.call()
    }
}