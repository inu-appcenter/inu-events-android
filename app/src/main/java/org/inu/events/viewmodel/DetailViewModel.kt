package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.data.model.Article
import org.inu.events.data.service.EventService
import org.inu.events.data.service.UserService
import org.inu.events.di.AppConfigs
import org.inu.events.util.SingleLiveEvent

class DetailViewModel : ViewModel() {
    //현재 표시할 게시물의 데이터가 저장돼있음
    private val _detailDataList = MutableLiveData<Article>()
    val detailDataList: MutableLiveData<Article>
        get() = _detailDataList

    //todo - 수민 : 일단 동작은 되는데 뭔가 아닌것같음,,,
    var eventIndex = -1
        private set

    val commentClickEvent = SingleLiveEvent<Int>()

    //서버 불러오기
    private val eventService: EventService = AppConfigs.eventService
    private val userService: UserService = AppConfigs.userService

    fun load(eventId: Int) {
        eventIndex = eventId
        loadDetailData()
    }

    //현재 표시할 게시물의 데이터를 가져옴
    private fun loadDetailData() {
        _detailDataList.value = eventService.getEventDetail(eventIndex)
    }

    //댓글버튼 클릭했을 때 이벤트
    fun onClickComment() {
        commentClickEvent.value = eventIndex
    }

    fun deleteWriting() {
        return eventService.deleteEvent(eventIndex)
    }

    //todo - 자신이 작성한 글인 경우 true, 아닌경우 false 반환
    //자신이 작성한 글인지 어떻게 알지?
    //event 테이블에 user_id가 그 글을 작성한 사람의 id
    //event.user_id == user.id 인 경우 자신이 작성한 글
    fun isMyWriting(): Boolean {
        //현재 접속해있는 user 정보 어디에저장하지??
        //if(_detailDataList.value?.user_id == userService.getUser())
        return true
    }
}