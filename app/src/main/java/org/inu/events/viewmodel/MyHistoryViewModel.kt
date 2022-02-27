package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyHistoryViewModel : ViewModel() {
    val title = MutableLiveData("내가 쓴 글")
}