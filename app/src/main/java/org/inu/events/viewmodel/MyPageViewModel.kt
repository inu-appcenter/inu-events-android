package org.inu.events.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.inu.events.*
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.entity.User
import org.inu.events.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyPageViewModel : ViewModel(), KoinComponent {
    private val userService: UserService by inject()
    val user = MutableLiveData<User>()
    val onClickBackEvent = SingleLiveEvent<Any>()

    val backButtonListener = object : BackButtonListener {
        override fun invoke(view: View) {
            onClickBackEvent.call()
        }
    }

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
        view.context.startActivity(intent)
    }

    fun onClickMyComment(view: View) {
        val intent = Intent(view.context, MyHistoryActivity::class.java)
        view.context.startActivity(intent)
    }
}