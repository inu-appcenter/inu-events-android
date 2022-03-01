package org.inu.events.service

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.inu.events.common.db.SharedPreferenceWrapper
import org.inu.events.common.threading.execute
import org.inu.events.data.model.dto.AddFcmParams
import org.inu.events.data.model.entity.Account
import org.inu.events.data.repository.AccountRepository
import org.inu.events.data.repository.FcmRepository
import org.inu.events.di.OkHttpClientFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

class LoginService(
    private val accountRepository: AccountRepository
): KoinComponent {
    private val _isLoggedIn = MutableLiveData(false)

    val isLoggedInLiveData: LiveData<Boolean> = _isLoggedIn
    val isLoggedIn: Boolean get() = _isLoggedIn.value!!

    private val fcmRepository: FcmRepository by inject()
    private val context : Context by inject()
    private val db = SharedPreferenceWrapper(context)

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
            postToken()
        }.catch {
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

    fun logout() {
        OkHttpClientFactory.clearCookie()
        accountRepository.clearAccount()
        _isLoggedIn.postValue(false)
    }

    private fun postToken(){
        execute {
            fcmRepository.postFcm(
                AddFcmParams( db.getString("fcmToken") ?: ""))
        }.then {
        }.catch {  }
    }
}