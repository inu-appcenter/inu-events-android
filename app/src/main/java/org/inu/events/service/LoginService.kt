package org.inu.events.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.inu.events.common.threading.execute
import org.inu.events.data.model.entity.Account
import org.inu.events.data.repository.AccountRepository

class LoginService(
    private val accountRepository: AccountRepository
) {
    private val _isLoggedIn = MutableLiveData(false)

    val isLoggedInLiveData: LiveData<Boolean> = _isLoggedIn
    val isLoggedIn: Boolean get() = _isLoggedIn.value!!

    /**
     * 액세스 토큰 가지고 로그인하기.
     * 로그인 성공 여부에 따라 [isLoggedIn] 값이 바뀜.
     */
    fun login(accessToken: String) {
        execute {
            accountRepository.login(accessToken)
        }.then {
            accountRepository.saveAccount(Account(it.userId, it.rememberMeToken))
            _isLoggedIn.postValue(true)
        }.catch {
            Log.e("!!", it.toString())
            _isLoggedIn.postValue(false)
        }
    }

    fun isAutoLoginPossible():Boolean {
        return accountRepository.getSavedAccount() != null
    }

    fun tryAutoLogin() {
        val account = accountRepository.getSavedAccount() ?: return
        execute {
            accountRepository.login(account)
        }.then {
            accountRepository.saveAccount(Account(it.userId, it.rememberMeToken))
            _isLoggedIn.postValue(true)
        }.catch {
            Log.e("자동 로그인 실패", it.toString())
            _isLoggedIn.postValue(false)
        }
    }
}