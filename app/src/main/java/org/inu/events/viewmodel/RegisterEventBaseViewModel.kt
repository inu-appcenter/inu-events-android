package org.inu.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.Event

open class RegisterEventBaseViewModel : ViewModel() {
    val eventMyActivity : MutableLiveData<Boolean> = MutableLiveData()
    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>>
        get() = _viewEvent
    fun viewEvent(content: Any) {
        _viewEvent.value = Event(content)
    }
}