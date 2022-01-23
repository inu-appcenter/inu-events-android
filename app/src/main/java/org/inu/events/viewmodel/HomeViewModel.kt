package org.inu.events.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.R
import org.inu.events.data.model.Article
import org.inu.events.data.service.EventService
import org.inu.events.di.AppConfigs
import org.inu.events.util.SingleLiveEvent
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {
    private val _homeDataList = MutableLiveData<List<Article>>()
    val homeDataList : LiveData<List<Article>>
        get() = _homeDataList

    val postClickEvent = SingleLiveEvent<Any>()

    private val eventService: EventService = AppConfigs.eventService

    init{
        _homeDataList.value = loadHomeData()
    }

    //TODO 지금은 임시 데이터
    private fun loadHomeData(): List<Article> {
        return eventService.fetchEvent()
    }

    fun onClickPost() {
        postClickEvent.call()
    }
}