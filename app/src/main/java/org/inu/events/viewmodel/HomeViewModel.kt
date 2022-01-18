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
    private val _homeDataList = MutableLiveData<ArrayList<HomeData>>()
    val homeDataList : LiveData<ArrayList<HomeData>>
        get() = _homeDataList

    private var homeData = ArrayList<HomeData>()

    val postClickEvent = SingleLiveEvent<Any>()

    val eventService: EventService = AppConfigs.eventService

    init{
        homeData = loadHomeData()
        _homeDataList.value = homeData
    }

    //TODO("서버나오면 수정해야 할 부분")
    //일단은 데이터가 없으니까 임시로 넣어줌
    private fun loadHomeData(): ArrayList<HomeData> {
        return eventService.fetchEvent()
    }

    fun onClickPost() {
        postClickEvent.call()
    }
}