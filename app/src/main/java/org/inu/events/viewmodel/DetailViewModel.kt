package org.inu.events.viewmodel

import androidx.lifecycle.ViewModel
import org.inu.events.util.SingleLiveEvent

class DetailViewModel : ViewModel() {


    val commentClickEvent = SingleLiveEvent<Any>()

    fun onClickComment() {
        commentClickEvent.call()
    }
}