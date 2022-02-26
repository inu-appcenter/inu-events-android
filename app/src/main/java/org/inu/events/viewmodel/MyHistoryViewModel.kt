package org.inu.events.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.BackButtonListener
import org.inu.events.common.util.SingleLiveEvent

class MyHistoryViewModel : ViewModel() {
    val onClickBackEvent = SingleLiveEvent<Any>()
    val title = MutableLiveData("내가 쓴 글")

    val backButtonListener = object : BackButtonListener {
        override fun invoke(view: View) {
            onClickBackEvent.call()
        }
    }
}