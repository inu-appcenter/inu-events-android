package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.data.model.entity.User

class BlockedAccountViewModel : ViewModel() {
    val blockedAccountList = MutableLiveData<List<User>>()

    fun onClickCancelBlocking(userId: Int) {

    }
}