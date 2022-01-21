package org.inu.events.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.data.model.Article
import org.inu.events.data.service.EventService
import org.inu.events.di.AppConfigs
import org.inu.events.util.SingleLiveEvent

class DetailViewModel : ViewModel() {
    private val _detailDataList = MutableLiveData<Article>()
    val detailDataList : MutableLiveData<Article>
        get() = _detailDataList

    //todo - 수민 : 일단 동작은 되는데 뭔가 아닌것같음,,,
    var eventIndex = MutableLiveData<Int>()
        set(value){
            Log.d("tag","eventIndex set $eventIndex")
            field = value
            field.value = value.value
            _detailDataList.value = loadDetailData()
        }

    val commentClickEvent = SingleLiveEvent<Any>()

    private val eventService: EventService = AppConfigs.eventService

    init{
        _detailDataList.value = loadDetailData()
    }

    private fun loadDetailData(): Article{
        return eventService.getEventDetail(eventIndex.value)
    }

    fun onClickComment() {
        commentClickEvent.call()
    }

    //todo - 자신이 작성한 글인 경우 true, 아닌경우 false 반환
    //자신이 작성한 글인지 어떻게 알지?
    //event 테이블에 user_id가 그 글을 작성한 사람의 id
    //event.user_id == user.id 인 경우 자신이 작성한 글
    fun isMyWriting(): Boolean{
        return false
    }
}