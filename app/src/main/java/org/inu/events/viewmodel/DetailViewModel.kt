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

    var startDate = MutableLiveData("")
    var endDate = MutableLiveData("")

    var eventIndex = MutableLiveData<Int>()
        set(value){
            Log.d("tag","eventIndex set ${eventIndex.value}")
            field = value
            field.value = value.value
            _detailDataList.value = loadDetailData()
            startDate.value = dateFormat(_detailDataList.value!!.start_at)
            endDate.value = dateFormat(_detailDataList.value!!.end_at)
        }

    val commentClickEvent = SingleLiveEvent<Int>()

    //서버 불러오기
    private val eventService: EventService = AppConfigs.eventService
    private val userService: UserService = AppConfigs.userService

    fun load(eventId: Int) {
        eventIndex = eventId
        loadDetailData()
    }

    private fun dateFormat(date:String) = "%s.%s.%s".format(date.slice(IntRange(0,3)),date.slice(IntRange(5,6)),date.slice(IntRange(8,9)))

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
    //event 테이블에 user_id로 getUser 요청을 해서 그 안에 있는 게시글 리스트 중에 현재 이벤트 id와 동일한 것이 있으면됨
    fun isMyWriting(): Boolean{
        //val currentEventUserIdGetUser = userService.getUser(_detailDataList.value?.user_id)
        //if()
        return true
    }
}