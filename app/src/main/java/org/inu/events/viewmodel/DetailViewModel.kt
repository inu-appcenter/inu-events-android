package org.inu.events.viewmodel

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.R
import org.inu.events.common.threading.execute
import org.inu.events.data.model.entity.Event
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.repository.CommentRepository
import org.inu.events.data.model.dto.NotificationParams
import org.inu.events.data.repository.EventRepository
import org.inu.events.data.repository.NotificationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*

class DetailViewModel : ViewModel(), KoinComponent {
    private val eventRepository: EventRepository by inject()
    private val commentRepository: CommentRepository by inject()
    private val notificationRepository: NotificationRepository by inject()

    //현재 표시할 게시물의 데이터가 저장돼있음
    private val _currentEvent = MutableLiveData<Event>()
    val currentEvent: MutableLiveData<Event>
        get() = _currentEvent

    var imageUrl = MutableLiveData("")
    var startDate = MutableLiveData("")
    var endDate = MutableLiveData("")
    var startTime = MutableLiveData("")
    var endTime = MutableLiveData("")
    val onOff = MutableLiveData(false)
    private val setFor = MutableLiveData("")

    var eventIndex = -1
        private set
    var eventWroteByMeBoolean = false
        private set
    var checkDeadline: Boolean = false

    val commentClickEvent = SingleLiveEvent<Int>()
    val alarmClickEvent = SingleLiveEvent<Any>()
    val onOffText = MutableLiveData<String>()
    val onOffColor = MutableLiveData<Int>(R.color.black80)
    val onOffBackground = MutableLiveData<Int>(R.color.white)
    val subMissionUrlNull = MutableLiveData(false)
    val contactNull = MutableLiveData(false)
    val bothNull = MutableLiveData(false)
    val commentSize = MutableLiveData("")
    val boardDateText = MutableLiveData("")
    val boardDateBackground = MutableLiveData<Int>(R.color.white)

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

    private fun whenDay(end_at: String?): String {
        if (end_at == null) return "D-??"

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        val endDate = dateFormat.parse(end_at).time
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        var dDay = (endDate - today) / (24 * 60 * 60 * 1000)

        if (dDay < 0) {
            checkDeadline = true
            return "마감"
        }
        return "D-$dDay"
    }

    private fun isDeadline(): Int = if (checkDeadline) {
        R.drawable.drawable_home_board_date_deadline_background
    } else {
        R.drawable.drawable_home_board_date_ongoing_background
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
            if(_currentEvent.value?.submissionUrl == null) subMissionUrlNull.value = true
            if(_currentEvent.value?.contact == null)contactNull.value = true
            if(subMissionUrlNull.value!! and contactNull.value!!) bothNull.value = true
            eventWroteByMeBoolean = it.wroteByMe ?:false
            onOff.value = it.notificationSetByMe ?: false
            onOffText.value = if (onOff.value!!) "알람 취소" else "알람 신청"
            onOffColor.value = if (onOff.value!!) R.color.primary100 else R.color.white
            onOffBackground.value = if (onOff.value!!) R.color.primary_base else R.color.primary100
            setFor.value = it.notificationSetFor ?: ""
            boardDateText.value = whenDay(it.endAt)
            boardDateBackground.value = isDeadline()
        }.catch {
            Log.i("error: loadDetailData",it.stackTrace.toString())
        }

        execute {
            commentRepository.getComments(eventIndex)
        }.then{
            commentSize.value = "댓글 ${it.size}개"
        }.catch {}

    }

    private fun loadNotificationButton(onOff:Boolean){
        onOffText.value = if (onOff) "알람 취소" else "알람 신청"
        onOffColor.value = if (onOff) R.color.primary100 else R.color.white
        onOffBackground.value = if (onOff) R.color.primary_base else R.color.primary100
    }


    //댓글버튼 클릭했을 때 이벤트
    fun onClickComment() {
        commentClickEvent.value = eventIndex
    }

    // 알람버튼 클릭했을 때 이벤트
    fun onClickButton(){
        alarmClickEvent.call()
    }


    fun postNotification(setFor:String){
        execute{
            notificationRepository.postNotification(
                NotificationParams(
                    eventId = eventIndex,
                    setFor = setFor
                )
            )
        }.then {
            onOff.value = true
            this.setFor.value = setFor
            Log.i("good: postNotification", onOff.value.toString())
            loadNotificationButton(onOff.value!!)
        }.catch {
            Log.i("error: postNotification",it.stackTrace.toString())
        }
    }

    fun deleteNotification(){
        execute {
            Log.i("execute: deleteNotification","$eventIndex, ${setFor.value}")
            notificationRepository.deleteNotification(
                NotificationParams(
                    eventId = eventIndex,
                    setFor = setFor.value!!
                )
            )
        }.then {
            onOff.value = false
            Log.i("good: deleteNotification", onOff.value.toString())
            loadNotificationButton(onOff.value!!)
        }.catch {
            Log.i("error: deleteNotification",it.stackTrace.toString())
        }
    }

    fun getNotifications(){
        execute {
            notificationRepository.getNotifications()
        }.then {
        }.catch {
        }
    }



    fun onDeleteClickEvent() {
        execute {
            eventRepository.deleteEvent(eventIndex)
        }.then {  }. catch {  }
    }

    fun isMyWriting() = true
}