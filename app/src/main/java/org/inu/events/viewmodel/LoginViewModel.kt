package org.inu.events.viewmodel

import androidx.lifecycle.ViewModel
import org.inu.events.common.util.SingleLiveEvent

class LoginViewModel : ViewModel() {

    val loginClickEvent = SingleLiveEvent<Any>()

    fun onClickLogin() {
        loginClickEvent.call()
    }
}