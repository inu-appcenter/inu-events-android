package org.inu.events.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.R
import org.inu.events.common.threading.execute
import org.inu.events.data.model.entity.Event
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.repository.EventRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DetailViewModel : ViewModel(), KoinComponent {
    private val eventRepository: EventRepository by inject()

    //현재 표시할 게시물의 데이터가 저장돼있음
    private val _currentEvent = MutableLiveData<Event>()
    val currentEvent: MutableLiveData<Event>
        get() = _currentEvent

    var imageUrl = MutableLiveData("")
    var startDate = MutableLiveData("")
    var endDate = MutableLiveData("")
    var startTime = MutableLiveData("")
    var endTime = MutableLiveData("")
    var fcmToggleButtonText = MutableLiveData("알람 받기")
    var fcmToggleButtonBackground = MutableLiveData(R.color.primary)
    var fcmToggleButtonTextColor = MutableLiveData(R.color.background)

    var eventIndex = -1
        private set

    private var fcmBoolean = false
    val commentClickEvent = SingleLiveEvent<Int>()
    val fcmClickEvent = SingleLiveEvent<Any>()

    fun load(eventId: Int) {
        eventIndex = eventId
        loadDetailData()
    }

    private fun dateFormat(date:String) = "%s.%s.%s".format(date.slice(IntRange(0,3)),date.slice(IntRange(5,6)),date.slice(IntRange(8,9)))
    private fun timeFormat(time:String): String{
        val hour = time.slice(IntRange(11,12))
        val minute = time.slice(IntRange(14,15))
        return "%s:%s %s".format(
            if (hour.toInt() > 12) (hour.toInt() - 12).toString() else hour,
            minute,
            if (hour.toInt() > 12) "PM" else "AM"
        )
    }

    //현재 표시할 게시물의 데이터를 가져옴
    private fun loadDetailData() {
        execute {
            eventRepository.getEvent(eventIndex)
        }.then {
            _currentEvent.value = it
            startDate.value = dateFormat(it.startAt)
            endDate.value = dateFormat(it.endAt)
            startTime.value = timeFormat(it.startAt)
            endTime.value = timeFormat(it.endAt)
            imageUrl.value = "http://uniletter.inuappcenter.kr/images/${_currentEvent.value!!.imageUuid}"
        }.catch {  }
    }

    //댓글버튼 클릭했을 때 이벤트
    fun onClickComment() {
        commentClickEvent.value = eventIndex
    }

    // 알람버튼 클릭했을 때 이벤트
    fun onClickFcm(){
        fcmBoolean = !fcmBoolean
        if (!fcmBoolean){
            fcmToggleButtonText.value = "알람 받기"
            fcmToggleButtonBackground.value = R.color.primary
            fcmToggleButtonTextColor.value = R.color.background
        }
        else{
            fcmToggleButtonText.value = "알람 취소하기"
            fcmToggleButtonBackground.value = R.color.background
            fcmToggleButtonTextColor.value = R.color.primary
        }
        fcmClickEvent.call()
    }

    fun onDeleteClickEvent() {
        execute {
            eventRepository.deleteEvent(eventIndex)
        }.then {  }. catch {  }

    }

    //todo - 자신이 작성한 글인 경우 true, 아닌경우 false 반환
    fun isMyWriting(): Boolean{

        return true
    }
}