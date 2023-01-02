package org.inu.events.ui.login

import androidx.lifecycle.ViewModel
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.service.LoginService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {

    private val loginService: LoginService by inject()

    val loginClickEvent = SingleLiveEvent<Any>()
    val loggedIn = loginService.isLoggedInLiveData

    fun onClickLogin() {
        loginClickEvent.call()
    }

    fun login(accessToken: String) {
        loginService.login(accessToken)
    }
}