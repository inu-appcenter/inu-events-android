package org.inu.events.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.R
import org.inu.events.data.HomeData
import org.inu.events.data.service.DummyEventService
import org.inu.events.data.service.EventService
import org.inu.events.di.AppConfigs
import org.inu.events.util.SingleLiveEvent

class HomeViewModel : ViewModel() {
    private val _homeDataList = MutableLiveData<List<HomeData>>()
    val homeDataList : LiveData<List<HomeData>>
        get() = _homeDataList

    private var homeData = emptyList<HomeData>()

    val postClickEvent = SingleLiveEvent<Any>()

    private val eventService: EventService = AppConfigs.eventService

    init{
        homeData = loadHomeData()
        _homeDataList.value = homeData
    }

    //TODO 지금은 임시 데이터
    private fun loadHomeData(): List<HomeData> {
        return eventService.fetchEvent()
    }

    fun onClickPost() {
        postClickEvent.call()
    }
}