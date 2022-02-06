package org.inu.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.common.threading.execute
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.inu.events.common.util.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {

    private val eventRepository: EventRepository by inject()

    private val _homeDataList = MutableLiveData<List<Event>>()
    val homeDataList: LiveData<List<Event>>
        get() = _homeDataList

    val postClickEvent = SingleLiveEvent<Any>()

    fun load() {
        loadHomeData()
    }

    private fun loadHomeData() {
        execute {
            eventRepository.getEvents()
        }.then {
            _homeDataList.value = it
        }.catch {  }
    }

    fun onClickPost() {
        postClickEvent.call()
    }
}