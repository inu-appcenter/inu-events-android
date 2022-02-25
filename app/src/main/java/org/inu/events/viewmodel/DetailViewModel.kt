package org.inu.events.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.R
import org.inu.events.common.threading.execute
import org.inu.events.data.model.entity.Event
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.LikeParam
import org.inu.events.data.repository.CommentRepository
import org.inu.events.data.model.dto.NotificationParams
import org.inu.events.data.repository.EventRepository
import org.inu.events.data.repository.LikeRepository
import org.inu.events.data.repository.NotificationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime

class DetailViewModel : ViewModel(), KoinComponent {
    private val eventRepository: EventRepository by inject()
    private val commentRepository: CommentRepository by inject()
    private val notificationRepository: NotificationRepository by inject()
    private val likeRepository: LikeRepository by inject()

    //현재 표시할 게시물의 데이터가 저장돼있음
    private val _currentEvent = MutableLiveData<Event>()
    val currentEvent: MutableLiveData<Event>
        get() = _currentEvent


    var imageUrl = MutableLiveData("")
    var startDate = MutableLiveData("")
    var endDate = MutableLiveData("")
    var startTime = MutableLiveData("")
    var endTime = MutableLiveData("")
    val notificationOnOff = MutableLiveData(false)
    val likeOnOff = MutableLiveData(false)
    private val notificationSetFor = MutableLiveData("")
    val likeButtonSource = MutableLiveData(R.drawable.img_like_off)

    var eventIndex = -1
        private set
    var eventWroteByMeBoolean = false
        private set

    val commentClickEvent = SingleLiveEvent<Int>()
    val alarmClickEvent = SingleLiveEvent<Int>()
    val notificationText = MutableLiveData<String>()
    val notificationColor = MutableLiveData<Int>(R.color.black80)
    val notificationBackground = MutableLiveData<Int>(R.color.white)
    val subMissionUrlNull = MutableLiveData(false)
    val contactNull = MutableLiveData(false)
    val bothNull = MutableLiveData(false)
    val commentSize = MutableLiveData("")
    var notificationQuarter = MutableLiveData(-1)


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
            if(_currentEvent.value?.submissionUrl == null) subMissionUrlNull.value = true
            if(_currentEvent.value?.contact == null)contactNull.value = true
            if(subMissionUrlNull.value!! and contactNull.value!!) bothNull.value = true
            eventWroteByMeBoolean = it.wroteByMe ?:false
            notificationOnOff.value = it.notificationSetByMe ?: false
            likeOnOff.value = it.likedByMe ?: false
            notificationText.value = if (notificationOnOff.value!!) "알람 취소" else "알람 신청"
            notificationColor.value = if (notificationOnOff.value!!) R.color.primary100 else R.color.white
            notificationBackground.value = if (notificationOnOff.value!!) R.color.primary_base else R.color.primary100
            notificationSetFor.value = it.notificationSetFor ?: ""
            notificationQuarter.value = timeComparison(LocalDateTime.now().toString(),it.startAt,it.endAt)
            likeButtonSource.value = if (likeOnOff.value == true)  R.drawable.img_like_on else R.drawable.img_like_off
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
        notificationText.value = if (onOff) "알람 취소" else "알람 신청"
        notificationColor.value = if (onOff) R.color.primary100 else R.color.white
        notificationBackground.value = if (onOff) R.color.primary_base else R.color.primary100
    }


    //댓글버튼 클릭했을 때 이벤트
    fun onClickComment() {
        commentClickEvent.value = eventIndex
    }

    // 알람버튼 클릭했을 때 이벤트
    fun onClickNotification(){
        alarmClickEvent.value = notificationQuarter.value
    }

    fun onClickLike(){
        if (likeOnOff.value == true){
            deleteLike()
            likeButtonSource.value = R.drawable.img_like_off
        }
        else{
            postLike()
            likeButtonSource.value = R.drawable.img_like_on
        }
        likeOnOff.value = likeOnOff.value != true
    }

    // 게시물의 시작시간과 마감시간 또 현재 시간을 비교하는 함수
    private fun timeComparison(now:String,startDate:String,endDate:String): Int{
        return when (startDate) {
            endDate -> when {       // 시작시간과 마감시간이 같은 행사
                now < startDate -> 1   // 지금이 시작 전이라면 시작 전 알림만
                else -> 0  // 지금이 시작한 후라면 비활성화
            }
            else -> when {   // 시작시간과 마감시간이 다른 행사
                now < startDate -> 3     // 지금이 시작 전이라면 시작전과 마감알림 모두
                startDate <= now && now < endDate -> 2  // 지금이 행사 시작후, 마감 전이라면 마감 전 알림만
                else -> 0   // 지금이 마감뒤라면 비활성화
            }
        }
    }

    private fun postLike(){
        execute {
            likeRepository.postLike(
                LikeParam(eventId = eventIndex)
            )
        }.then {
        }.catch {  }
    }

    fun deleteLike(){
        execute {
            likeRepository.deleteLike(
                LikeParam(eventId = eventIndex)
            )
        }.then {
        }.catch {  }
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
            notificationOnOff.value = true
            this.notificationSetFor.value = setFor
            loadNotificationButton(notificationOnOff.value!!)
        }.catch {
        }
    }

    fun deleteNotification(){
        execute {
            notificationRepository.deleteNotification(
                NotificationParams(
                    eventId = eventIndex,
                    setFor = notificationSetFor.value!!
                )
            )
        }.then {
            notificationOnOff.value = false
            loadNotificationButton(notificationOnOff.value!!)
        }.catch {
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