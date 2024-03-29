package org.inu.events.ui.mypage

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.entity.User
import org.inu.events.service.LoginService
import org.inu.events.service.UserService
import org.inu.events.ui.mypage.info.OpenSourceLicensesActivity
import org.inu.events.ui.mypage.info.PrivacyPolicyActivity
import org.inu.events.ui.mypage.setting.NotificationSettingActivity
import org.inu.events.ui.mypage.shortcut.BlockedAccountActivity
import org.inu.events.ui.mypage.shortcut.MyHistoryActivity
import org.inu.events.ui.mypage.store.LikeActivity
import org.inu.events.ui.mypage.store.NotificationActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyPageViewModel : ViewModel(), KoinComponent {
    private val userService: UserService by inject()
    val loginService: LoginService by inject()
    val user = MutableLiveData<User>()
    val loginEvent = SingleLiveEvent<Any>()

    fun fetchData() {
        CoroutineScope(Dispatchers.Main).launch {
            user.postValue(userService.getUserInfo())
        }
    }

    fun onClickProfileUpdate(view: View) {
        val intent = Intent(view.context, UpdateProfileActivity::class.java)
        view.context.startActivity(intent)
    }

    fun onClickNotificationSetting(view: View) {
        val intent = Intent(view.context, NotificationSettingActivity::class.java)
        view.context.startActivity(intent)
    }

    fun onClickPrivacyPolicy(view: View) {
        val intent = Intent(view.context, PrivacyPolicyActivity::class.java)
        view.context.startActivity(intent)
    }

    fun onClickOpenSourceLicenses(view: View) {
        val intent = Intent(view.context, OpenSourceLicensesActivity::class.java)
        view.context.startActivity(intent)
    }

    fun onClickLikeList(view: View) {
        val intent = Intent(view.context, LikeActivity::class.java)
        view.context.startActivity(intent)
    }

    fun onClickNotificationList(view: View) {
        val intent = Intent(view.context, NotificationActivity::class.java)
        view.context.startActivity(intent)
    }

    fun onClickMyEvent(view: View) {
        val intent = Intent(view.context, MyHistoryActivity::class.java)
        intent.putExtra("isEvent", true)
        view.context.startActivity(intent)
    }

    fun onClickMyComment(view: View) {
        val intent = Intent(view.context, MyHistoryActivity::class.java)
        intent.putExtra("isEvent", false)
        view.context.startActivity(intent)
    }

    fun onClickBlockedAccount(view: View) {
        val intent = Intent(view.context, BlockedAccountActivity::class.java)
        view.context.startActivity(intent)
    }

    fun logout() {
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) { loginService.logout() }
            deferred.await()
            loginEvent.call()
        }
    }
}